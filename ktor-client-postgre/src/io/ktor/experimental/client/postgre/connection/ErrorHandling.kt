package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.sql.*
import kotlinx.io.core.*


internal fun checkErrors(type: BackendMessage, payload: ByteReadPacket): SqlQueryResult = when (type) {
    BackendMessage.ERROR_RESPONSE -> {
        throw payload.readException()
    }
    BackendMessage.NOTICE_RESPONSE -> {
        val notice = payload.readException()
        println("NOTICE: $notice")
        SqlEmptyResult
    }
    else -> throw UnknownPostgrePacketTypeException(type)
}

