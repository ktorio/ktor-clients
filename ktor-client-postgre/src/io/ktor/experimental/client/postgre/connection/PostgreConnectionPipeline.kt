package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.sql.utils.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.sockets.Socket
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.io.*
import kotlinx.io.core.*
import java.lang.IllegalStateException
import java.net.*
import kotlin.coroutines.*

@UseExperimental(KtorExperimentalAPI::class)
private val POSTGRE_SELECTOR_MANAGER = ActorSelectorManager(Dispatchers.Default)

internal fun CoroutineContext.PostgreConnectionPipeline(
    address: InetSocketAddress, database: String,
    user: String, password: String?,
    requests: ReceiveChannel<SqlRequest>
): SqlConnectionPipeline = PostgreConnectionPipeline(
    address, database, user, password, requests, this
).apply {
    start()
}

private class PostgreConnectionPipeline(
    private val address: InetSocketAddress,
    private val database: String,
    private val user: String,
    private val password: String?,
    requests: ReceiveChannel<SqlRequest>,
    coroutineContext: CoroutineContext
) : SqlConnectionPipeline(requests, coroutineContext = coroutineContext), CoroutineScope {

    private lateinit var socket: Socket
    private lateinit var input: ByteReadChannel
    private lateinit var output: ByteWriteChannel

    private lateinit var properties: Map<String, String>

    internal fun start() {
        writer.start()
    }

    override suspend fun onStart() {
        super.onStart()
        socket = aSocket(POSTGRE_SELECTOR_MANAGER)
            .tcp().tcpNoDelay()
            .connect(address)

        input = socket.openReadChannel()
        output = socket.openWriteChannel()

        output.writePostgreStartup(user, database)
        negotiate()
    }

    override suspend fun send(callScope: CoroutineScope, request: String, requestType: RequestType) {
        when (requestType){
            RequestType.QUERY -> {
                output.writePostgrePacket(FrontendMessage.QUERY) {
                    writeCString(request)
                }
            }
            RequestType.PREPARE -> {
                output.writePostgrePacket(FrontendMessage.PARSE) {
                }
            }
        }
    }

    override suspend fun receive(callScope: CoroutineScope): SqlQueryResult {
        val responseHead = input.nextResponseHead()

        val result = with(callScope) {
            simpleReceivePipeline(responseHead, input) ?: extendedReceivePipeline(responseHead, input)
        }

        result?.let { return it }
        return checkErrors(responseHead.type, responseHead.payload)
    }

    override fun onDone() {
        super.onDone()
        socket.close()
    }

    override fun onError(cause: Throwable) {
        super.onError(cause)

        println("Connection fail: $cause")
        cause.printStackTrace()
    }

    private suspend fun negotiate() {
        loop@ while (true) {
            val packet = input.readPostgrePacket()
            val size = packet.size
            val payload = packet.payload
            val activeProperties = mutableMapOf<String, String>()

            when (packet.type) {
                BackendMessage.AUTHENTICATION_REQUEST -> {
                    val authType = AuthenticationType.fromCode(payload.readInt())
                    when (authType) {
                        AuthenticationType.OK -> continue@loop
                        AuthenticationType.MD5_PASSWORD -> {
                            check(password != null)
                            check(payload.remaining == 4L) {
                                "Received md5 salt size is invalid: expected 4 actual ${payload.remaining}."
                            }

                            val salt = payload.readBytes(4)
                            output.authMD5(user, password, salt)
                        }
                        else -> error("Unsupported auth format: $authType")
                    }
                }
                BackendMessage.READY_FOR_QUERY -> {
                    check(size == 1)
                    /* ignored in negotiate transaction status indicator */
                    payload.readByte()
                    properties = activeProperties
                    return
                }
                BackendMessage.BACKEND_KEY_DATA -> {
                    val backendPID = payload.readInt()
                    val backendSecret = payload.readInt()

                    activeProperties["PID"] = backendPID.toString()
                    activeProperties["Secret"] = backendSecret.toString()
                }
                BackendMessage.PARAMETER_STATUS -> {
                    val key = payload.readCString()
                    val value = payload.readCString()
                    activeProperties[key] = value
                }
                else -> checkErrors(packet.type, payload)
            }

            check(payload.remaining == 0L)
        }
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class UnknownPostgrePacketTypeException internal constructor(
    type: BackendMessage
) : IllegalStateException("Type: $type")
