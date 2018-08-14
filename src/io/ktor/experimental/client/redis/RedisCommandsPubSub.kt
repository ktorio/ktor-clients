package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*

interface RedisPubSub {
    data class Packet(val pattern: Boolean, val message: Boolean, val channel: String, val content: String)
}

internal suspend fun Redis.pubsub(): RedisPubSub = TODO()

suspend fun RedisPubSub.channel(): Channel<RedisPubSub.Packet> = TODO()

suspend fun RedisPubSub.psubscribe(vararg patterns: String): Unit = TODO()

suspend fun RedisPubSub.punsubscribe(vararg patterns: String): Unit = TODO()

suspend fun RedisPubSub.subscribe(vararg channels: String): Unit = TODO()

suspend fun RedisPubSub.unsubscribe(vararg channels: String): Unit = TODO()

suspend fun RedisPubSub.publish(channel: String, message: String): Unit = TODO()

suspend fun RedisPubSub.pubsub(command: String, vararg arguments: Any?): Unit = TODO()
