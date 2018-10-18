package io.ktor.experimental.client.redis.utils

import kotlinx.io.pool.*
import java.nio.*

internal const val DEFAULT_REDIS_BUFFER_SIZE = 4096

private const val DEFAULT_REDIS_POOL_CAPACITY = 1024

internal object RedisBufferPool : DefaultPool<ByteBuffer>(DEFAULT_REDIS_POOL_CAPACITY) {
    override fun produceInstance(): ByteBuffer = ByteBuffer.allocate(DEFAULT_REDIS_BUFFER_SIZE)

    override fun clearInstance(instance: ByteBuffer): ByteBuffer = instance.apply { clear() }
}

internal object RedisCharBufferPool : DefaultPool<CharBuffer>(DEFAULT_REDIS_POOL_CAPACITY) {
    override fun produceInstance(): CharBuffer = CharBuffer.allocate(DEFAULT_REDIS_BUFFER_SIZE)
    override fun clearInstance(instance: CharBuffer): CharBuffer = instance.apply { clear() }
}
