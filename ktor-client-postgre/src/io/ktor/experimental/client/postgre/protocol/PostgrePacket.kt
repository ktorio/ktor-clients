package io.ktor.experimental.client.postgre.protocol

import io.ktor.experimental.client.postgre.*
import kotlinx.coroutines.io.*
import kotlinx.io.core.*

internal class PostgrePacket(
    val type: BackendMessage,
    val payload: ByteReadPacket
) {
    val size: Int = payload.remaining.toInt()

    override fun toString(): String = "PostgrePacket($type(len=$size)"
}

internal suspend fun ByteReadChannel.readPostgrePacket(startUp: Boolean = false): PostgrePacket {
    val type = BackendMessage.fromValue(if (!startUp) readByte() else 0)
    val payloadSize = readInt() - 4
    if (payloadSize < 0) throw IllegalStateException("readPostgrePacket: type=$type, payloadSize=$payloadSize")
    return PostgrePacket(type, readPacket(payloadSize))
}

internal suspend fun ByteWriteChannel.writePostgreStartup(
    user: String,
    database: String = user,
    vararg params: Pair<String, String>
) {
    writePostgrePacket(FrontendMessage.STARTUP_MESSAGE) {
        /**
         * The protocol version number.
         * The most significant 16 bits are the major version number (3 for the protocol described here).
         * The least significant 16 bits are the minor version number (0 for the protocol described here).
         */
        writeInt(0x0003_0000)
        val pairArray = mutableMapOf<String, String>().apply {
            put("user", user)
            put("database", database)
            put("application_name", "ktor-cio") 
            put("client_encoding", "UTF8")
            putAll(params)
        }
        for ((key, value) in pairArray) {
            writeCString(key)
            writeCString(value)
        }
        writeCString("")
    }
}

internal suspend fun ByteWriteChannel.writePostgrePacket(type: FrontendMessage, block: BytePacketBuilder.() -> Unit) {
    if (type.code != 0.toChar()) writeByte(type.code.toByte())
    val packet = buildPacket { block() }

    writeInt(4 + packet.remaining)
    writePacket(packet)
    flush()
}

