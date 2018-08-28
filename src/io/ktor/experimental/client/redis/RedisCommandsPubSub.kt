package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*

interface RedisPubSub {
    interface Packet
    data class Subscription(val channel: String, val subscriptions: Long, val subscribe: Boolean, val isPattern: Boolean = false) : Packet
    data class Message(val channel: String, val message: String, val isPattern: Boolean = false) : Packet
    //data class Packet(val channel: String, val content: String, val isPattern: Boolean, val isMessage: Boolean)
}

interface RedisPubSubInternal : RedisPubSub {
    val redis: Redis
}

internal class RedisPubSubImpl(override val redis: Redis) : RedisPubSubInternal {
    internal val rawChannel = redis.run { Redis.InternalChannel.run { getMessageChannel() } }
    internal val channel = rawChannel.map {
        val list = it as List<Any>
        val kind = list[0].toString()
        val channel = list[1].toString()
        val info = list[2].toString()
        val isPattern = kind.startsWith("p")
        val isSubscription = kind.startsWith("psub") || kind.startsWith("sub")
        val isMessage = kind == "message"
        if (isMessage) {
            RedisPubSub.Message(channel, info, isPattern)
        } else {
            RedisPubSub.Subscription(channel, info.toLong(), isSubscription, isPattern)
        }
    }
}

/**
 * Starts a new pubsub session.
 */
private suspend fun Redis._pubsub(): RedisPubSub = RedisPubSubImpl(this)

/**
 * Listen for messages published to channels matching the given patterns
 *
 * https://redis.io/commands/psubscribe
 *
 * @since 2.0.0
 */
suspend fun Redis.psubscribe(vararg patterns: String): RedisPubSub = _pubsub().psubscribe(*patterns)

/**
 * Listen for messages published to the given channels
 *
 * https://redis.io/commands/subscribe
 *
 * @since 2.0.0
 */
suspend fun Redis.subscribe(vararg channels: String): RedisPubSub = _pubsub().psubscribe(*channels)

/**
 * Listen for messages published to channels matching the given patterns
 *
 * https://redis.io/commands/psubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.psubscribe(vararg patterns: String): RedisPubSub =
    this.apply { (this as RedisPubSubImpl).redis.executeTyped("PSUBSCRIBE", *patterns) }

/**
 * Listen for messages published to the given channels
 *
 * https://redis.io/commands/subscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.subscribe(vararg channels: String): RedisPubSub =
    this.apply { (this as RedisPubSubImpl).redis.executeTyped("SUBSCRIBE", *channels) }

/**
 * Gets the a channel of packets for this client subscription.
 */
suspend fun RedisPubSub.channel(): ReceiveChannel<RedisPubSub.Packet> = (this as RedisPubSubImpl).channel

suspend fun RedisPubSub.messagesChannel(): ReceiveChannel<RedisPubSub.Message> = channel().map { it as? RedisPubSub.Message? }.filterNotNull()

suspend fun RedisPubSub.subscriptionChannel(): ReceiveChannel<RedisPubSub.Subscription> = channel().map { it as? RedisPubSub.Subscription? }.filterNotNull()

/**
 * Stop listening for messages posted to channels matching the given patterns
 *
 * https://redis.io/commands/punsubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.punsubscribe(vararg patterns: String): RedisPubSub =
    this.apply { (this as RedisPubSubImpl).redis.executeTyped("PUNSUBSCRIBE", *patterns) }

/**
 * Stop listening for messages posted to the given channels
 *
 * https://redis.io/commands/unsubscribe
 *
 * @since 2.0.0
 */
suspend fun RedisPubSub.unsubscribe(vararg channels: String): RedisPubSub =
    this.apply { (this as RedisPubSubImpl).redis.executeTyped("UNSUBSCRIBE", *channels) }

/**
 * Post a message to a channel
 *
 * https://redis.io/commands/publish
 *
 * @since 2.0.0
 */
suspend fun Redis.publish(channel: String, message: String): Long =
    (this as RedisPubSubInternal).redis.executeTyped("PUBLISH", channel, message)

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
    (this as RedisPubSubInternal).redis.executeArrayString(*arrayOfNotNull("PUBSUB", "CHANNELS", pattern))

/**
 * Returns the number of subscribers (not counting clients subscribed to patterns) for the specified channels.
 *
 * https://redis.io/commands/pubsub#codepubsub-numsub-channel-1--channel-ncode
 *
 * @since 2.8.0
 */
suspend fun RedisPubSub.pubsubNumsub(vararg channels: String): Map<String, Long> =
    (this as RedisPubSubInternal).redis.executeArrayString("PUBSUB", "NUMSUB", *channels).toListOfPairsString()
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
    (this as RedisPubSubInternal).redis.executeTyped("PUBSUB", "NUMPAT")
