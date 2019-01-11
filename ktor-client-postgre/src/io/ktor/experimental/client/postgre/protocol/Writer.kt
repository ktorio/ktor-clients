package io.ktor.experimental.client.postgre.protocol

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.util.*
import kotlinx.coroutines.io.*
import java.security.*

internal suspend fun ByteWriteChannel.authPlainPassword(password: String) {
    writePostgrePacket(FrontendMessage.PASSWORD_MESSAGE) {
        writeCString(password)
    }
}

internal suspend fun ByteWriteChannel.authMD5(user: String, password: String, salt: ByteArray) {
    val encoder = MessageDigest.getInstance("MD5")!!

    fun md5(password: String, salt: ByteArray): String {
        encoder.update(password.toByteArray())
        encoder.update(salt)

        return encoder.digest().toHex()
    }

    writePostgrePacket(FrontendMessage.PASSWORD_MESSAGE) {
        writeCString("md5${md5(md5(password, user.toByteArray()), salt)}")
    }
}