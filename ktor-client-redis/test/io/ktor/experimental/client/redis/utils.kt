package io.ktor.experimental.client.redis

import kotlinx.coroutines.*
import kotlinx.coroutines.io.*
import kotlinx.io.core.*
import java.net.*

internal fun redisTest(
    address: InetSocketAddress,
    password: String? = null,
    maxConnections: Int = 50,
    block: suspend Redis.() -> Unit
) = runBlocking {
    RedisClient(address, password = password, maxConnections = maxConnections).use { redis ->
        redis.block()
    }
}

internal fun buildChannel(block: BytePacketBuilder.() -> Unit): ByteReadChannel {
    val content = buildPacket(block = block).readBytes()
    return ByteReadChannel(content)
}
