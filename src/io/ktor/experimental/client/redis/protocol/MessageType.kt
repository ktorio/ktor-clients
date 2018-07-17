package io.ktor.experimental.client.redis.protocol

internal val EOL = "\r\n".toByteArray()

internal enum class RedisType(val code: Byte) {
    STRING('+'.toByte()),
    NUMBER(':'.toByte()),
    BULK('$'.toByte()),
    ARRAY('*'.toByte()),
    ERROR('-'.toByte());

    companion object {
        fun fromCode(code: Byte): RedisType =
            types.find { it.code == code } ?: throw RedisException("No suitable message type found")

        val types: Array<RedisType> = RedisType.values()
    }
}

class RedisException(message: String) : Exception(message)
