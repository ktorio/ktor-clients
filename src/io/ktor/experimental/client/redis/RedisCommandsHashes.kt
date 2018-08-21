package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*

/**
 * Delete one or more hash fields
 *
 * https://redis.io/commands/hdel
 *
 * @since 2.0.0
 */
suspend fun Redis.hdel(key: String, field: String): Boolean = commandBool("HDEL", key, field)

/**
 * Delete one or more hash fields
 *
 * https://redis.io/commands/hdel
 *
 * @since 2.4.0
 */
suspend fun Redis.hdel(key: String, field: String, vararg fields: String): Long = commandLong("HDEL", key, field, *fields)

/**
 * Determine if a hash field exists
 *
 * https://redis.io/commands/hexists
 *
 * @since 2.0.0
 */
suspend fun Redis.hexists(key: String, field: String): Boolean = commandBool("HEXISTS", key, field)

/**
 * Get the value of a hash field
 *
 * https://redis.io/commands/hget
 *
 * @since 2.0.0
 */
suspend fun Redis.hget(key: String, field: String): String? = commandString("HGET", key, field)

/**
 * Get all the fields and values in a hash
 *
 * https://redis.io/commands/hgetall
 *
 * @since 2.0.0
 */
suspend fun Redis.hgetall(key: String): Map<String, String> = commandArrayString("HGETALL", key).listOfPairsToMap()

/**
 * Increment the integer value of a hash field by the given number
 *
 * https://redis.io/commands/hincrby
 *
 * @since 2.0.0
 */
suspend fun Redis.hincrby(key: String, field: String, increment: Long): Long =
    commandLong("HINCRBY", key, field, "$increment")

/**
 * Increment the float value of a hash field by the given amount
 *
 * https://redis.io/commands/hincrbyfloat
 *
 * @since 2.0.0
 */
suspend fun Redis.hincrbyfloat(key: String, field: String, increment: Double): Double =
    commandDouble("HINCRBYFLOAT", key, field, "$increment")

/**
 * Get all the fields in a hash
 *
 * https://redis.io/commands/hkeys
 *
 * @since 2.0.0
 */
suspend fun Redis.hkeys(key: String): Set<String> = commandArrayString("HKEYS", key).toSet()

/**
 * Get the number of fields in a hash
 *
 * https://redis.io/commands/hlen
 *
 * @since 2.0.0
 */
suspend fun Redis.hlen(key: String): Long = commandLong("HLEN", key)

/**
 * Get the values of all the given hash fields
 *
 * https://redis.io/commands/hmget
 *
 * @since 2.0.0
 */
suspend fun Redis.hmget(key: String, vararg fields: String): List<String> = if (fields.isNotEmpty()) commandArrayString("HMGET", key, *fields) else listOf()

/**
 * Set multiple hash fields to multiple values
 *
 * https://redis.io/commands/hmset
 *
 * @since 2.0.0
 */
suspend fun Redis.hmset(key: String, vararg pairs: Pair<String, String>) = if (pairs.isNotEmpty()) commandUnit("HMSET", key, *pairs.flatMap { listOf(it.first, it.second) }.toTypedArray()) else Unit

/**
 * Set multiple hash fields to multiple values
 *
 * https://redis.io/commands/hmset
 *
 * @since 2.0.0
 */
suspend fun Redis.hmset(key: String, map: Map<String, String>) = hmset(key, *map.map { it.key to it.value }.toTypedArray())

/**
 * Set the string value of a hash field
 *
 * https://redis.io/commands/hset
 *
 * @since 2.0.0
 */
suspend fun Redis.hset(key: String, field: String, value: String): Boolean = commandBool("HSET", key, field, value)

/**
 * Set the string value of a hash field
 *
 * https://redis.io/commands/hset
 *
 * @since 2.0.0
 */
suspend fun Redis.hset(key: String, vararg pairs: Pair<String, String>): Int {
    return pairs.map { hset(key, it.first, it.second) }.count { it }
}

/**
 * Set the value of a hash field, only if the field does not exist
 *
 * https://redis.io/commands/hsetnx
 *
 * @since 2.0.0
 */
suspend fun Redis.hsetnx(key: String, field: String, value: String): Boolean = commandBool("HSETNX", key, field, value)

/**
 * Get the length of the value of a hash field
 *
 * https://redis.io/commands/hstrlen
 *
 * @since 3.2.0
 */
suspend fun Redis.hstrlen(key: String, field: String): Long = commandLong("HSTRLEN", key, field)

/**
 * Get all the values in a hash
 *
 * https://redis.io/commands/hvals
 *
 * @since 2.0.0
 */
suspend fun Redis.hvals(key: String): Set<String> = commandArrayString("HVALS", key).toSet()

/**
 * Incrementally iterate hash fields and associated values
 *
 * https://redis.io/commands/hscan
 *
 * @since 2.8.0
 */
suspend fun Redis.hscan(key: String, pattern: String? = null): ReceiveChannel<Pair<String, String>> =
    _scanBasePairs("HSCAN", key, pattern)
