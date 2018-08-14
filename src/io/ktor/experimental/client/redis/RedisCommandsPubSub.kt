package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*

interface RedisPubSub {
    data class Packet(val pattern: Boolean, val message: Boolean, val channel: String, val content: String)
}

interface RedisPubSubInternal : RedisPubSub {
    val redis: Redis
}

/**
 * Starts a new pubsub session.
 */
internal suspend fun Redis.pubsub(): RedisPubSub = TODO()

/**
 * Listen for messages published to channels matching the given patterns
 *
 * https://redis.io/commands/psubscribe
 *
 * @since 2.0.0
 */
internal suspend fun Redis.psubscribe(vararg patterns: String): RedisPubSub = pubsub().psubscribe(*patterns)

/**
 * Listen for messages published to the given channels
 *
 * https://redis.io/commands/subscribe
 *
 * @since 2.0.0
 */
internal suspend fun Redis.subscribe(vararg channels: String): RedisPubSub = pubsub().psubscribe(*channels)

/**
 * Gets the a channel of packets for this client subscription.
 */
suspend fun RedisPubSub.channel(): Channel<RedisPubSub.Packet> = TODO()

/**
 * Listen for messages published to channels matching the given patterns
 *
 * https://redis.io/commands/psubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.psubscribe(vararg patterns: String): RedisPubSub = TODO()

/**
 * Stop listening for messages posted to channels matching the given patterns
 *
 * https://redis.io/commands/punsubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.punsubscribe(vararg patterns: String): RedisPubSub = TODO()

/**
 * Listen for messages published to the given channels
 *
 * https://redis.io/commands/subscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.subscribe(vararg channels: String): RedisPubSub = TODO()

/**
 * Stop listening for messages posted to the given channels
 *
 * https://redis.io/commands/unsubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.unsubscribe(vararg channels: String): RedisPubSub = TODO()

/**
 * Post a message to a channel
 *
 * https://redis.io/commands/publish
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.publish(channel: String, message: String): Long =
    (this as RedisPubSubInternal).redis.commandLong("publish", channel, message)

/**
 * Lists the currently active channels.
 * An active channel is a Pub/Sub channel with one or more subscribers (not including clients subscribed to patterns).
 * If no pattern is specified, all the channels are listed, otherwise if pattern is specified only channels matching
 * the specified glob-style pattern are listed.
 *
 * https://redis.io/commands/pubsub#pubsub-channels-pattern
 *
 * @since 2.8.0
 */
suspend fun RedisPubSub.pubsubChannels(pattern: String?): List<String> =
    (this as RedisPubSubInternal).redis.commandArrayString(*arrayOfNotNull("pubsub", "channels", pattern))

/**
 * Returns the number of subscribers (not counting clients subscribed to patterns) for the specified channels.
 *
 * https://redis.io/commands/pubsub#codepubsub-numsub-channel-1--channel-ncode
 *
 * @since 2.8.0
 */
suspend fun RedisPubSub.pubsubNumsub(vararg channels: String): Map<String, Long> =
    (this as RedisPubSubInternal).redis.commandArrayString("pubsub", "numsub", *channels).toListOfPairsString()
        .map { it.first to it.second.toLong() }.toMap()

/**
 * Returns the number of subscriptions to patterns (that are performed using the PSUBSCRIBE command).
 * Note that this is not just the count of clients subscribed to patterns but the total number of patterns
 * all the clients are subscribed to.
 *
 * https://redis.io/commands/pubsub#codepubsub-numpatcode
 *
 * @since 2.8.0
 */
suspend fun RedisPubSub.pubsubNumpat(): Long =
    (this as RedisPubSubInternal).redis.commandLong("pubsub", "numpat")
