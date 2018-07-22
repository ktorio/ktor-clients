package io.ktor.experimental.client.redis

/**
 * Delete one or more hash fields
 *
 * https://redis.io/commands/hdel
 *
 * @since 2.0.0
 */
suspend fun Redis.hdel(key: String, field: String): Boolean = commandBool("hdel", key, field)

/**
 * Delete one or more hash fields
 *
 * https://redis.io/commands/hdel
 *
 * @since 2.4.0
 */
suspend fun Redis.hdel(key: String, field: String, vararg fields: String): Long = commandLong("hdel", key, field, *fields)

/**
 * Determine if a hash field exists
 *
 * https://redis.io/commands/hexists
 *
 * @since 2.0.0
 */
suspend fun Redis.hexists(key: String, field: String): Boolean = commandBool("hexists", key, field)

/**
 * Get the value of a hash field
 *
 * https://redis.io/commands/hget
 *
 * @since 2.0.0
 */
suspend fun Redis.hget(key: String, field: String): String? = commandString("hget", key, field)

/**
 * Get all the fields and values in a hash
 *
 * https://redis.io/commands/hgetall
 *
 * @since 2.0.0
 */
suspend fun Redis.hgetall(key: String): Map<String, String> = commandArrayString("hgetall", key).listOfPairsToMap()

/**
 * Increment the integer value of a hash field by the given number
 *
 * https://redis.io/commands/hincrby
 *
 * @since 2.0.0
 */
suspend fun Redis.hincrby(key: String, field: String, increment: Long): Long =
    commandLong("hincrby", key, field, "$increment")

/**
 * Increment the float value of a hash field by the given amount
 *
 * https://redis.io/commands/hincrbyfloat
 *
 * @since 2.0.0
 */
suspend fun Redis.hincrbyfloat(key: String, field: String, increment: Double): Double =
    commandDouble("hincrbyfloat", key, field, "$increment")

/**
 * Get all the fields in a hash
 *
 * https://redis.io/commands/hkeys
 *
 * @since 2.0.0
 */
suspend fun Redis.hkeys(key: String): Set<String> = commandArrayString("hkeys", key).toSet()

/**
 * Get the number of fields in a hash
 *
 * https://redis.io/commands/hlen
 *
 * @since 2.0.0
 */
suspend fun Redis.hlen(key: String): Long = commandLong("hlen", key)

/**
 * Get the values of all the given hash fields
 *
 * https://redis.io/commands/hmget
 *
 * @since 2.0.0
 */
suspend fun Redis.hmget(key: String, vararg fields: String): List<String> = if (fields.isNotEmpty()) commandArrayString("hmget", key, *fields) else listOf()

/**
 * Set multiple hash fields to multiple values
 *
 * https://redis.io/commands/hmset
 *
 * @since 2.0.0
 */
suspend fun Redis.hmset(key: String, vararg pairs: Pair<String, String>) = if (pairs.isNotEmpty()) commandUnit("hmset", key, *pairs.flatMap { listOf(it.first, it.second) }.toTypedArray()) else Unit

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
suspend fun Redis.hset(key: String, field: String, value: String): Boolean = commandBool("hset", key, field, value)

/**
 * Set the value of a hash field, only if the field does not exist
 *
 * https://redis.io/commands/hsetnx
 *
 * @since 2.0.0
 */
suspend fun Redis.hsetnx(key: String, field: String, value: String): Boolean = commandBool("hsetnx", key, field, value)

/**
 * Get the length of the value of a hash field
 *
 * https://redis.io/commands/hstrlen
 *
 * @since 3.2.0
 */
suspend fun Redis.hstrlen(key: String, field: String): Long = commandLong("hstrlen", key, field)

/**
 * Get all the values in a hash
 *
 * https://redis.io/commands/hvals
 *
 * @since 2.0.0
 */
suspend fun Redis.hvals(key: String): Set<String> = commandArrayString("hvals", key).toSet()

/**
 * Incrementally iterate hash fields and associated values
 *
 * https://redis.io/commands/hscan
 *
 * @since 2.8.0
 */
suspend fun Redis.hscan(key: String, cursor: Long, match: String? = null, count: Long? = null): Unit = TODO()
