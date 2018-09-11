package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.io.*
import kotlinx.io.core.*
import java.net.*

internal fun redisTest(
    address: InetSocketAddress,
    password: String? = null,
    block: suspend Redis.() -> Unit
) = runBlocking {
    RedisClient(address, password = password).use { redis ->
        redis.block()
    }
}

internal fun buildChannel(block: BytePacketBuilder.() -> Unit): ByteReadChannel {
    val content = buildPacket(block = block).readBytes()
    return ByteReadChannel(content)
}
