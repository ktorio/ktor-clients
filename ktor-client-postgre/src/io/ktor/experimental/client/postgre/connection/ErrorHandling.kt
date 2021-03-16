package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.utils.io.core.*


internal fun checkErrors(type: BackendMessage, payload: ByteReadPacket): Unit = when (type) {
    BackendMessage.ERROR_RESPONSE -> {
        throw payload.readException()
    }
    BackendMessage.NOTICE_RESPONSE -> {
        val notice = payload.readException()
    }
    else -> throw UnknownPostgrePacketTypeException(type)
}

