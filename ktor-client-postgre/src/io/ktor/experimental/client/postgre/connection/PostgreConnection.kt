package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.postgre.scheme.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.util.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.sockets.Socket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.io.*
import kotlinx.io.core.*
import org.slf4j.*
import java.lang.IllegalStateException
import java.net.*
import kotlin.coroutines.*

private val POSTGRE_SELECTOR_MANAGER = ActorSelectorManager(Dispatchers.Default)

class PostgreConnection(
    private val address: InetSocketAddress,
    private val database: String,
    private val user: String,
    private val password: String?,
    requests: ReceiveChannel<SqlRequest>,
    coroutineContext: CoroutineContext
) : ConnectionPipeline<String, QueryResult>(requests, coroutineContext = coroutineContext), CoroutineScope {

    private lateinit var socket: Socket
    private lateinit var input: ByteReadChannel
    private lateinit var output: ByteWriteChannel

    private lateinit var properties: Map<String, String>

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

    override suspend fun send(callScope: CoroutineScope, request: String) {
        output.writePostgrePacket(FrontendMessage.QUERY) {
            writeCString(request)
        }
    }

    override suspend fun receive(callScope: CoroutineScope): QueryResult {
        val firstPacket = input.readPostgrePacket()

        return callScope.trySimplePipeline(firstPacket, input)
            ?: callScope.tryExtendedPipeline(firstPacket, input)
            ?: error("Unexpected packet: type ${firstPacket.type}, $firstPacket")
    }

    private fun CoroutineScope.tryExtendedPipeline(
        firstPacket: PostgrePacket,
        input: ByteReadChannel
    ): QueryResult? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDone() {
        super.onDone()
        socket.close()
    }

    override fun onError(cause: Throwable) {
        super.onError(cause)

        println("Connection fail: $cause")
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
