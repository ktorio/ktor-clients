package io.ktor.experimental.client.redis

/**
 * Adds the specified elements to the specified HyperLogLog.
 *
 * https://redis.io/commands/pfadd
 *
 * @since 2.8.9
 */
suspend fun Redis.pfadd(key: String, vararg elements: String): Long = executeTyped("PFADD", key, *elements)

/**
 * Return the approximated cardinality of the set(s) observed by the HyperLogLog at key(s).
 *
 * https://redis.io/commands/pfcount
 *
 * @since 2.8.9
 */
suspend fun Redis.pfcount(vararg keys: String): Long = executeTyped("PFCOUNT", *keys)

/**
 * Merge N different HyperLogLogs into a single one.
 *
 * https://redis.io/commands/pfmerge
 *
 * @since 2.8.9
 */
suspend fun Redis.pfmerge(destKey: String, vararg sourceKeys: String): Unit = executeTyped(
    "PFMERGE",
    destKey,
    *sourceKeys
)
