package io.ktor.experimental.client.postgre.protocol

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.postgre.scheme.*
import kotlinx.io.core.*

internal fun ByteReadPacket.readException(): PostgreException {
    val details = mutableListOf<Pair<Char, String>>()

    while (remaining > 0) {
        val type = readByte()

        if (type == 0.toByte()) {
            check(remaining == 0L) { "There are some remaining bytes in exception message: $remaining" }
            break
        }

        val message = readCString()
        details += type.toChar() to message
    }

    return PostgreException(details)
}

internal fun ByteReadPacket.readColumns(): List<PostgreColumn> {
    val size = readShort().toInt() and 0xffff
    return List(size) {
        PostgreColumn(
            name = readCString(),
            tableOID = readInt(),
            attributeID = readShort().toInt() and 0xffff,
            typeOID = readInt(),
            typeSize = readShort().toInt(), /* typeSize can be negative */
            typeMod = readInt(),
            // The format code being used for the field. Currently will be zero (text) or one (binary). 
            // In a RowDescription returned from the statement variant of Describe, the format code is not yet known
            // and will always be zero.
            text = readShort().toInt() == 0
        )
    }
}

internal fun ByteReadPacket.readRow(): List<ByteArray?> {
    val size = readShort().toInt() and 0xffff
    return List(size) {
        val cellSize = readInt()
        if (cellSize < 0) null else readBytes(cellSize)
    }
}
