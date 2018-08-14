package io.ktor.experimental.client.redis

/**
 * Remove and get the first element in a list, or block until one is available
 *
 * https://redis.io/commands/blpop
 *
 * @since 2.0.0
 */
suspend fun Redis.blpop(vararg keys: String, timeoutMs: Int = 0): Long = commandLong("blpop", *keys, timeoutMs)

/**
 * Remove and get the last element in a list, or block until one is available
 *
 * https://redis.io/commands/brpop
 *
 * @since 2.0.0
 */
suspend fun Redis.brpop(vararg keys: String, timeoutMs: Int = 0): Long = commandLong("brpop", *keys, timeoutMs)

/**
 * Pop a value from a list, push it to another list and return it; or block until one is available
 *
 * https://redis.io/commands/brpoplpush
 *
 * @since 2.2.0
 */
suspend fun Redis.brpoplpush(src: String, dst: String, timeoutMs: Int = 0): Long = commandLong("brpoplpush", src, dst, timeoutMs)

/**
 * Remove the last element in a list, prepend it to another list and return it
 *
 * https://redis.io/commands/rpoplpush
 *
 * @since 1.2.0
 */
suspend fun Redis.rpoplpush(src: String, dst: String): Long = commandLong("rpoplpush", src, dst)

/**
 * Get an element from a list by its index
 *
 * https://redis.io/commands/lindex
 *
 * @since 1.0.0
 */
suspend fun Redis.lindex(key: String, index: Int): String? = commandString("lindex", key, index)

/**
 * Insert an element before another element in a list
 *
 * https://redis.io/commands/linsert
 *
 * @since 2.2.0
 */
suspend fun Redis.linsertBefore(key: String, pivot: String, value: Any?): Long = commandLong("linsert", key, "before", pivot, value)

/**
 * Insert an element after another element in a list
 *
 * https://redis.io/commands/linsert
 *
 * @since 2.2.0
 */
suspend fun Redis.linsertAfter(key: String, pivot: String, value: Any?): Long = commandLong("linsert", key, "after", pivot, value)

/**
 * Get the length of a list
 *
 * https://redis.io/commands/llen
 *
 * @since 1.0.0
 */
suspend fun Redis.llen(key: String): Long = commandLong("llen", key)

/**
 * Remove and get the first element in a list
 *
 * https://redis.io/commands/lpop
 *
 * @since 1.0.0
 */
suspend fun Redis.lpop(key: String): String? = commandString("lpop", key)

/**
 * Remove and get the last element in a list
 *
 * https://redis.io/commands/rpop
 *
 * @since 1.0.0
 */
suspend fun Redis.rpop(key: String): String? = commandString("rpop", key)

/**
 * Insert all the specified values at the head of the list stored at key.
 *
 * https://redis.io/commands/lpush
 *
 * @since 1.0.0
 */
suspend fun Redis.lpush(key: String, vararg values: String): Long = commandLong("lpush", key, *values)

/**
 * Prepend a value to a list, only if the list exists
 *
 * https://redis.io/commands/lpushx
 *
 * @since 2.2.0
 */
suspend fun Redis.lpushx(key: String, value: String): Long = commandLong("lpushx", key, value)

/**
 * Append one or multiple values to a list
 *
 * https://redis.io/commands/rpush
 *
 * @since 1.0.0
 */
suspend fun Redis.rpush(key: String, vararg values: String): Long = commandLong("rpush", key, *values)

/**
 * Append a value to a list, only if the list exists
 *
 * https://redis.io/commands/rpushx
 *
 * @since 2.2.0
 */
suspend fun Redis.rpushx(key: String, value: String): Long = commandLong("rpushx", key, value)

/**
 * Get a range of elements from a list
 *
 * https://redis.io/commands/lrange
 *
 * @since 1.0.0
 */
suspend fun Redis.lrange(key: String, start: Long, stop: Long): List<String> = commandArrayString("lrange", key, start, stop)

/**
 * Remove elements from a list
 *
 * https://redis.io/commands/lrem
 *
 * @since 1.0.0
 */
suspend fun Redis.lrem(key: String, count: Long, value: String): Long = commandLong("lrem", key, count, value)

/**
 * Set the value of an element in a list by its index
 *
 * https://redis.io/commands/lset
 *
 * @since 1.0.0
 */
suspend fun Redis.lset(key: String, index: Long, value: String) = commandUnit("lset", key, index, value)

/**
 * Trim a list to the specified range
 *
 * https://redis.io/commands/ltrim
 *
 * @since 1.0.0
 */
suspend fun Redis.ltrim(key: String, start: Long, stop: Long) = commandUnit("ltrim", key, start, stop)
