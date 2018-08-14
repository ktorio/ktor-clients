package io.ktor.experimental.client.redis

/**
 * https://redis.io/commands/xadd
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xadd(todo: Any): Unit = TODO()

/**
 * Returns the number of entries inside a stream.
 *
 * https://redis.io/commands/xlen
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xlen(key: String): Long = commandLong("xlen", key)

/**
 * https://redis.io/commands/xpending
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xpending(todo: Any): Unit = TODO()

/**
 * https://redis.io/commands/xrange
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xrange(todo: Any): Unit = TODO()

/**
 * https://redis.io/commands/xread
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xread(todo: Any): Unit = TODO()

/**
 * https://redis.io/commands/xreadgroup
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xreadgroup(todo: Any): Unit = TODO()

/**
 * https://redis.io/commands/xrevrange
 *
 * @since 5.0.0
 */
internal suspend fun Redis.xrevrange(todo: Any): Unit = TODO()
