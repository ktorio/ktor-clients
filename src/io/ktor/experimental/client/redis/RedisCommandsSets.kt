package io.ktor.experimental.client.redis

/**
 * Get the number of members in a set
 *
 * https://redis.io/commands/scard
 *
 * @since 1.0.0
 */
suspend fun Redis.scard(key: String): Long = commandLong("scard", key)

/**
 * Add one or more members to a set
 *
 * https://redis.io/commands/sadd
 *
 * @since 1.0.0
 */
suspend fun Redis.sadd(key: String, vararg members: String): Long = commandLong("sadd", key, *members)

/**
 * Remove one or more members from a set
 *
 * https://redis.io/commands/srem
 *
 * @since 1.0.0
 */
suspend fun Redis.srem(key: String, vararg members: String): Boolean = commandBool("srem", key, *members)

/**
 * Determine if a given value is a member of a set
 *
 * https://redis.io/commands/sismember
 *
 * @since 1.0.0
 */
suspend fun Redis.sismember(key: String, member: String): Boolean = commandBool("sismember", key, member)

/**
 * Get all the members in a set
 *
 * https://redis.io/commands/smembers
 *
 * @since 1.0.0
 */
suspend fun Redis.smembers(key: String): Set<String> = commandArrayString("smembers", key).toSet()

/**
 * Subtract multiple sets
 *
 * Returns the members of the set resulting from the difference between the first set and all the successive sets.
 *
 * https://redis.io/commands/sdiff
 *
 * @since 1.0.0
 */
suspend fun Redis.sdiff(key: String, vararg keys: String): Set<String> = commandArrayString("sdiff", key, *keys).toSet()

/**
 * Subtract multiple sets and store the resulting set in a key
 *
 * https://redis.io/commands/sdiffstore
 *
 * @since 1.0.0
 */
suspend fun Redis.sdiffstore(destination: String, key: String, vararg keys: String): Long = commandLong("sdiffstore", destination, key, *keys)

/**
 * Intersect multiple sets
 *
 * https://redis.io/commands/sinter
 *
 * @since 1.0.0
 */
suspend fun Redis.sinter(key: String, vararg keys: String): Set<String> = commandArrayString("sinter", key, *keys).toSet()

/**
 * Intersect multiple sets and store the resulting set in a key
 *
 * https://redis.io/commands/sinterstore
 *
 * @since 1.0.0
 */
suspend fun Redis.sinterstore(destination: String, key: String, vararg keys: String): Long = commandLong("sinterstore", destination, key, *keys)

/**
 * Add multiple sets
 *
 * https://redis.io/commands/sunion
 *
 * @since 1.0.0
 */
suspend fun Redis.sunion(key: String, vararg keys: String): Set<String> = commandArrayString("sunion", key, *keys).toSet()

/**
 * Add multiple sets and store the resulting set in a key
 *
 * https://redis.io/commands/sunionstore
 *
 * @since 1.0.0
 */
suspend fun Redis.sunionstore(destination: String, key: String, vararg keys: String): Long = commandLong("sunionstore", destination, key, *keys)

/**
 * Move a member from one set to another
 *
 * https://redis.io/commands/smove
 *
 * @since 1.0.0
 */
suspend fun Redis.smove(source: String, destination: String, member: String): Boolean = commandBool("smove", source, destination, member)

/**
 * Remove and return one or multiple random members from a set
 *
 * https://redis.io/commands/spop
 *
 * @since 1.0.0
 */
suspend fun Redis.spop(key: String): String? = commandString("spop", key)

/**
 * Remove and return one or multiple random members from a set
 *
 * https://redis.io/commands/spop
 *
 * @since 1.0.0
 */
suspend fun Redis.spop(key: String, count: Long): Set<String> = commandArrayString("spop", key, count).toSet()

/**
 * Get one or multiple random members from a set
 *
 * https://redis.io/commands/srandmember
 *
 * @since 1.0.0
 */
suspend fun Redis.srandmember(key: String): String? = commandString("srandmember", key)

/**
 * Get one or multiple random members from a set
 *
 * https://redis.io/commands/srandmember
 *
 * @since 1.0.0
 */
suspend fun Redis.srandmember(key: String, count: Long): Set<String> = commandArrayString("srandmember", key, count).toSet()

/**
 * Incrementally iterate Set elements
 *
 * https://redis.io/commands/sscan
 *
 * @since 2.8.0
 */
suspend fun Redis.sscan(key: String, cursor: Long, match: String? = null, count: Long? = null): Unit = TODO()
