package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.postgre.scheme.*
import io.ktor.experimental.client.postgre.util.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.sockets.*
import io.ktor.network.sockets.Socket
import io.ktor.pipeline.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.io.*
import kotlinx.io.core.*
import java.net.*

private val POSTGRE_SELECTOR_MANAGER = ActorSelectorManager(Dispatchers.Default)

class PostgreConnection(
    val address: InetSocketAddress,
    val user: String, val password: String?,
    val database: String,
    requests: ReceiveChannel<PipelineElement<String, PostgreRawResponse>>
) : ConnectionPipeline<String, PostgreRawResponse>(requests) {
    private lateinit var socket: Socket
    private lateinit var input: ByteReadChannel
    private lateinit var output: ByteWriteChannel

    private lateinit var properties: Map<String, String>

    override suspend fun onStart() {
        socket = aSocket(POSTGRE_SELECTOR_MANAGER)
            .tcp().tcpNoDelay()
            .connect(address)

        input = socket.openReadChannel()
        output = socket.openWriteChannel()

        output.writePostgreStartup(user, database)
        negotiate()
    }

    override suspend fun send(request: String) {
        output.writePostgrePacket(FrontendMessage.QUERY) {
            writeCString(request)
        }
    }

    override suspend fun receive(): PostgreRawResponse {
        var info = ""
        val rows = mutableListOf<List<ByteArray?>>()
        var columns: List<PostgreColumn> = emptyList()
        var notice: PostgreException? = null

        read@ while (true) {
            val packet = input.readPostgrePacket()
            val payload = packet.payload

            when (packet.type) {
                BackendMessage.ROW_DESCRIPTION -> {
                    columns = payload.readColumns()
                }
                BackendMessage.DATA_ROW -> {
                    rows += payload.readRow()
                }
                BackendMessage.COMMAND_COMPLETE -> {
                    info = payload.readCString()
                    break@read
                }
                BackendMessage.ERROR_RESPONSE -> {
                    throw payload.readException()
                }
                BackendMessage.NOTICE_RESPONSE -> {
                    notice = payload.readException()
                }
                BackendMessage.READY_FOR_QUERY -> {
                    check(payload.remaining == 1L)
                    /* ignored in negotiate transaction status indicator */
                    payload.readByte()
                }
                else -> error("Unsupported message type: ${packet.type}")
            }

            check(payload.remaining == 0L)
        }

        return PostgreRawResponse(
            info, notice, columns, rows
        )
    }

    override fun onDone() {
        socket.close()
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
                            val currentPassword = password ?: throw PostgreConnectException("Password required.")

                            output.authMD5(user, currentPassword, salt)
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
                BackendMessage.NOTICE_RESPONSE -> {
                    // TODO: log instead
                    throw payload.readException()
                }
                BackendMessage.ERROR_RESPONSE -> {
                    throw payload.readException()
                }
                else -> error("Unsupported packet type in negotiate: ${packet.type}")
            }

            check(payload.remaining == 0L)
        }
    }
}

class PostgreConnectException(override val message: String) : RuntimeException()