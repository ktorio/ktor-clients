package io.ktor.experimental.client.redis

/**
 * @since 5.0.0
 */
internal suspend fun Redis.bzpopmax(vararg keys: String, timeout: Long = 0): List<String> = TODO()

/**
 * @since 5.0.0
 */
internal suspend fun Redis.bzpopmin(vararg keys: String, timeout: Long = 0): List<String> = TODO()

/**
 * Add one or more members to a sorted set, or update its score if it already exists
 *
 * https://redis.io/commands/zadd
 *
 * @since 1.2.0
 */
suspend fun Redis.zadd(key: String, vararg scores: Pair<String, Double>): Long {
    val args = kotlin.collections.arrayListOf<Any?>()
    for (score in scores) {
        args += score.second
        args += score.first
    }
    return commandLong("zadd", key, *args.toTypedArray())
}

/**
 * Add one or more members to a sorted set, or update its score if it already exists
 *
 * https://redis.io/commands/zadd
 *
 * @since 1.2.0
 */
suspend fun Redis.zadd(key: String, scores: Map<String, Double>): Long =
    zadd(key, *scores.map { it.key to it.value }.toTypedArray())

/**
 * Add one or more members to a sorted set, or update its score if it already exists
 *
 * https://redis.io/commands/zadd
 *
 * @since 1.2.0
 */
suspend fun Redis.zadd(key: String, member: String, score: Double): Long = commandLong("zadd", key, score, member)

/**
 * Get the number of members in a sorted set
 *
 * https://redis.io/commands/zcard
 *
 * @since 1.2.0
 */
suspend fun Redis.zcard(key: String): Long = commandLong("zcard", key)

/**
 * Count the members in a sorted set with scores within the given values
 *
 * https://redis.io/commands/zcount
 *
 * @since 1.2.0
 */
suspend fun Redis.zcount(
    key: String,
    min: Double = Double.NEGATIVE_INFINITY,
    max: Double = Double.POSITIVE_INFINITY,
    includeMin: Boolean = true,
    includeMax: Boolean = true
): Long = commandLong("zcount", key, min.toRedisRange(includeMin), max.toRedisRange(includeMax))

/**
 * Increment the score of a member in a sorted set
 *
 * https://redis.io/commands/zincrby
 *
 * @since 1.2.0
 */
suspend fun Redis.zincrby(key: String, member: String, increment: Double) =
    commandString("zincrby", key, increment, member)!!

/**
 * Intersect multiple sorted sets and store the resulting sorted set in a new key
 *
 * https://redis.io/commands/zinterstore
 *
 * @since 2.0.0
 */
internal suspend fun Redis.zinterstore(todo: Any = TODO()): Long = TODO()

/**
 * Count the number of members in a sorted set between a given lexicographical range
 *
 * https://redis.io/commands/zlexcount
 *
 * @since 2.8.9
 */
suspend fun Redis.zlexcount(key: String, min: String, max: String): Long = commandLong("zlexcount", key, min, max)

/**
 * Removes and returns up to count members with the highest scores in the sorted set stored at key.
 *
 * https://redis.io/commands/zpopmax
 *
 * @since 5.0.0
 */
suspend fun Redis.zpopmax(key: String, count: Long = 1): Map<String, Double> =
    commandArrayString("zpopmax", key, count).listOfPairsToMap().map { it.value to it.key.toDouble() }.toMap()

/**
 * Removes and returns up to count members with the lowest scores in the sorted set stored at key.
 *
 * https://redis.io/commands/zpopmin
 *
 * @since 5.0.0
 */
suspend fun Redis.zpopmin(key: String, count: Long = 1): Map<String, Double> =
    commandArrayString("zpopmin", key, count).listOfPairsToMap().map { it.value to it.key.toDouble() }.toMap()

/**
 * Return a range of members in a sorted set, by index
 *
 * https://redis.io/commands/zrange
 *
 * @since 1.2.0
 */
suspend fun Redis.zrange(key: String, start: Long, stop: Long): Map<String, Double> =
    commandArrayString("zrange", key, start, stop, "WITHSCORES").listOfPairsToMap()
        .mapValues { it.value.toDouble() }

/**
 * Return a range of members in a sorted set, by lexicographical range
 *
 * https://redis.io/commands/zrangebylex
 *
 * @since 2.8.9
 */
suspend fun Redis.zrangebylex(key: String, min: String, max: String, limit: LongRange? = null): List<String> =
    commandArrayString(*arrayOf("zrangebylex", key, min, max) + (if (limit != null) arrayOf("LIMIT", "${limit.start}", "${limit.endInclusive - limit.start + 1}") else arrayOf()))

/**
 * Return a range of members in a sorted set, by score
 *
 * https://redis.io/commands/zrangebylex
 *
 * @since 1.0.5
 */
suspend fun Redis.zrangebyscore(key: String, min: Double, max: Double, limit: LongRange? = null): Map<String, Double> =
    commandArrayString(*arrayOf("zrangebyscore", key, min.toRedisString(), min.toRedisString(), "WITHSCORES") + (if (limit != null) arrayOf("LIMIT", "${limit.start}", "${limit.endInclusive - limit.start + 1}") else arrayOf()))
        .listOfPairsToMap()
        .mapValues { it.value.toDouble() }


/**
 * Determine the index of a member in a sorted set
 *
 * https://redis.io/commands/zrank
 *
 * @since 2.0.0
 */
suspend fun Redis.zrank(key: String, member: String): Long = commandLong("zrank", key, member)

/**
 * Remove one or more members from a sorted set
 *
 * https://redis.io/commands/zrem
 *
 * @since 1.2.0
 */
suspend fun Redis.zrem(key: String, member: String): Boolean = commandBool("zrem", key, member)

/**
 * Remove one or more members from a sorted set
 *
 * https://redis.io/commands/zrem
 *
 * @since 1.2.0
 */
suspend fun Redis.zrem(key: String, vararg members: String): Map<String, Double> =
    commandArrayString("zrem", *members).toListOfPairsString().map { it.first to it.second.toDouble() }.toMap()

/**
 * Remove all members in a sorted set between the given lexicographical range
 *
 * https://redis.io/commands/zremrangebylex
 *
 * @since 2.8.9
 */
suspend fun Redis.zremrangebylex(key: String, min: String, max: String): Long =
    commandLong("zremrangebylex", key, min ,max)

/**
 * Remove all members in a sorted set within the given indexes
 *
 * https://redis.io/commands/zremrangebyrank
 *
 * @since 2.8.9
 */
suspend fun Redis.zremrangebyrank(key: String, start: Long, stop: Long): Long =
    commandLong("zremrangebyrank", key, start, stop)

/**
 * Remove all members in a sorted set within the given scores
 *
 * https://redis.io/commands/zremrangebyscore
 *
 * @since 1.2.0
 */
suspend fun Redis.zremrangebyscore(key: String, min: Double, max: Double): Long =
    commandLong("zremrangebyscore", key, min.toRedisString(), max.toRedisString())

/**
 * Return a range of members in a sorted set, by index, with scores ordered from high to low (with scores)
 *
 * https://redis.io/commands/zrevrange
 *
 * @since 1.2.0
 */
suspend fun Redis.zrevrange(key: String, start: Long, stop: Long): Map<String, Double> =
    commandArrayString("zrevrange", key, start, stop, "WITHSCORES").listOfPairsToMap()
        .mapValues { it.value.toDouble() }

/**
 * Return a range of members in a sorted set, by lexicographical range, ordered from higher to lower strings.
 *
 * https://redis.io/commands/zrevrangebylex
 *
 * @since 2.8.9
 */
suspend fun Redis.zrevrangebylex(key: String, min: String, max: String, limit: LongRange? = null): List<String> =
    commandArrayString(*arrayOf("zrevrangebylex", key, min, max) + (if (limit != null) arrayOf("LIMIT", "${limit.start}", "${limit.endInclusive - limit.start + 1}") else arrayOf()))

/**
 * Return a range of members in a sorted set, by score, with scores ordered from high to low
 *
 * https://redis.io/commands/zrevrangebyscore
 *
 * @since 2.2.0
 */
suspend fun Redis.zrevrangebyscore(key: String, min: Double, max: Double, limit: LongRange? = null): Map<String, Double> =
    commandArrayString(*arrayOf("zrevrangebyscore", key, min.toRedisString(), min.toRedisString(), "WITHSCORES") + (if (limit != null) arrayOf("LIMIT", "${limit.start}", "${limit.endInclusive - limit.start + 1}") else arrayOf()))
        .listOfPairsToMap()
        .mapValues { it.value.toDouble() }
/**
 * Determine the index of a member in a sorted set, with scores ordered from high to low
 *
 * https://redis.io/commands/zrevrank
 *
 * @since 2.0.0
 */
suspend fun Redis.zrevrank(key: String, member: String): Long = commandLong("zrevrank", key, member)

/**
 * Incrementally iterate sorted sets elements and associated scores
 *
 * https://redis.io/commands/zscan
 *
 * @since 2.8.0
 */
internal suspend fun Redis.zscan(key: String, cursor: Long, match: String? = null, count: Long? = null): Unit = TODO()

/**
 * Get the score associated with the given member in a sorted set
 *
 * https://redis.io/commands/zscore
 *
 * @since 1.2.0
 */
suspend fun Redis.zscore(key: String, member: String): Double = commandDouble("zscore", key, member)

/**
 * Add multiple sorted sets and store the resulting sorted set in a new key
 *
 * https://redis.io/commands/zunionstore
 *
 * @since 2.0.0
 */
internal suspend fun Redis.zunionstore(todo: Any = TODO()): Long = TODO()

private fun Double.toRedisString() = when {
    isInfinite() -> if (this >= 0) "+inf" else "-inf"
    else -> "$this"
}

private fun Double.toRedisRange(include: Boolean): String {
    val out = when {
        isInfinite() -> if (this >= 0) "+inf" else "-inf"
        else -> "$this"
    }
    return if (include) out else "($out"
}
