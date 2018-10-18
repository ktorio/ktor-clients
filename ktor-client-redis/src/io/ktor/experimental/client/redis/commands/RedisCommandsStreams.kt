/**
 * https://redis.io/topics/streams-intro
 */

package io.ktor.experimental.client.redis

import kotlinx.coroutines.channels.*

/**
 * https://redis.io/commands/xadd
 *
 * @since 5.0.0
 */
suspend fun Redis.xadd(stream: String, vararg values: Pair<String, String>, id: String = "*", maxlen: Long? = null, maxlenApproximate: Boolean = true): String =
    executeTypedNull<String>(*arrayListOf<Any?>().apply {
        check(values.isNotEmpty())
        add("XADD")
        add(stream)
        if (maxlen != null) {
            add("MAXLEN")
            if (maxlenApproximate) add("~")
            add(maxlen)
        }
        add(id)
        for ((k, v) in values) {
            add(k)
            add(v)
        }
    }.toTypedArray()) ?: ""

/**
 * https://redis.io/commands/xtrim
 *
 * @since 5.0.0
 */
suspend fun Redis.xtrim(stream: String, maxlen: Long, approximate: Boolean = true): Unit {
    executeBuildNull<Unit> {
        add("XTRIM")
        add(stream)
        add("MAXLEN")
        if (approximate) add("~")
        add(maxlen)
    }
}

/**
 * Creates/ensures an empty Redis stream by inserting and deleting a dummy element
 */
suspend fun Redis.xcreate(stream: String) = xdel(stream, xadd(stream, "dummy" to "dummy"))

/**
 * https://redis.io/commands/xdel
 *
 * @since 5.0.0
 */
suspend fun Redis.xdel(stream: String, id: String): Unit =
    executeTyped("XDEL", stream, id)

/**
 * Returns the number of entries inside a stream.
 *
 * https://redis.io/commands/xlen
 *
 * @since 5.0.0
 */
suspend fun Redis.xlen(stream: String): Long = executeTyped("XLEN", stream)

/**
 * https://redis.io/commands/xrange
 *
 * @since 5.0.0
 */
suspend fun Redis.xrange(stream: String, start: String = "-", end: String = "+", count: Int? = null): Map<String, Map<String, String>> =
    _xrange("XRANGE", stream, start, end, count)

/**
 * https://redis.io/commands/xrevrange
 *
 * @since 5.0.0
 */
suspend fun Redis.xrevrange(stream: String, end: String = "+", start: String = "-", count: Int? = null): Map<String, Map<String, String>> =
    _xrange("XREVRANGE", stream, end, start, count)

/**
 * Reads in normal or reverse direction a range of items in a channel as a stream reading it in chunks
 * while they are being processed
 *
 * https://redis.io/commands/xrange
 *
 * @since 5.0.0
 */
suspend fun Redis.xrangeChannel(stream: String, start: String = "-", end: String = "+", chunkSize: Int = 32, reverse: Boolean = false): ReceiveChannel<Pair<String, Map<String, String>>> {
    return produce(context, chunkSize * 2) {
        var current = if (reverse) end else start
        do {
            val chunk = when (reverse) {
                false -> xrange(stream, current, end, count = chunkSize)
                true -> xrevrange(stream, start, current, count = chunkSize)
            }
            for ((k ,v) in chunk) {
                send(k to v)
            }
            val lastKey = chunk.keys.last()
            current = redisXnextId(lastKey, if (reverse) -1 else +1)
        } while (chunk.size >= chunkSize)
    }
}

/**
 * Alias for xrange key item item COUNT 1
 *
 * @since 5.0.0
 */
suspend fun Redis.xget(stream: String, item: String): Pair<String, Map<String, String>> =
    xrange(stream, item, item, 1).entries.first().let { it.key to it.value }

/**
 * https://redis.io/commands/xack
 *
 * @param group Consumer Group
 *
 * @since 5.0.0
 */
suspend fun Redis.xack(stream: String, group: String, vararg items: String): Int =
    executeTyped("XACK", stream, group, *items)

/**
 * https://redis.io/commands/xpending
 *
 * @since 5.0.0
 */
suspend fun Redis.xpending(
    stream: String, group: String, start: String = "-", end: String = "+", count: Int = 10, consumer: String? = null
) = _xrange(arrayListOf<Any?>().apply {
    add("XPENDING")
    add(stream)
    add(group)
    add(start)
    add(end)
    add(count)
    if (consumer != null) {
        add(consumer)
    }
})

/**
 * https://redis.io/commands/xclaim
 *
 * @since 5.0.0
 */
suspend fun Redis.xclaim(
    stream: String,
    group: String,
    consumer: String,
    minIdleTime: Int,
    vararg ids: String
) = _xrange(arrayListOf<Any?>().apply {
    add("XCLAIM")
    add(stream)
    add(group)
    add(consumer)
    add(minIdleTime)
    addAll(ids)
})

/**
 * https://redis.io/commands/xread
 *
 * @since 5.0.0
 */
// XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...]
suspend fun Redis.xread(
    vararg streamIds: Pair<String, String>,
    count: Int? = null,
    blockMs: Int? = null
): Map<String, Map<String, Map<String, String>>> {
    return xparseListGroup(executeBuildNull<Any> {
        add("XREAD")
        if (blockMs != null) {
            add("BLOCK")
            add(blockMs)
        }
        if (count != null) {
            add("COUNT")
            add(count)
        }
        add("STREAMS")
        for (v in streamIds) add(v.first)
        for (v in streamIds) add(v.second)
    } as List<Any>)
}

/**
 * https://redis.io/commands/xreadgroup
 *
 * @since 5.0.0
 */
//XREADGROUP GROUP group consumer [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...]
suspend fun Redis.xreadgroup(
    vararg streamIds: Pair<String, String>,
    group: String, consumer: String,
    count: Int? = null,
    blockMs: Int? = null
): Map<String, Map<String, Map<String, String>>> {
    return xparseListGroup(executeBuildNull<Any> {
        add("XREADGROUP")
        add("GROUP")
        add(group)
        add(consumer)
        if (blockMs != null) {
            add("BLOCK")
            add(blockMs)
        }
        if (count != null) {
            add("COUNT")
            add(count)
        }
        add("STREAMS")
        for (v in streamIds) add(v.first)
        for (v in streamIds) add(v.second)
        //println("xreadgroup: $this")
    } as List<Any>)
}
/**
 * https://redis.io/commands/xinfo-help
 *
 * @since 5.0.0
 */
suspend fun Redis.xinfoHelp(): List<String> = executeArrayString("XINFO", "HELP")

/**
 * https://redis.io/commands/xinfo-stream
 *
 * @since 5.0.0
 */
suspend fun Redis.xinfoStream(stream: String): RedisStreamInfo =
    RedisStreamInfo(executeArrayAny("XINFO", "STREAM", stream).listOfPairsToMapAny() as Map<String, Any?>)

/**
 * https://redis.io/commands/xinfo-groups
 *
 * @since 5.0.0
 */
suspend fun Redis.xinfoGroups(stream: String): List<RedisStreamGroupInfo> =
    executeArrayAny("XINFO", "GROUPS", stream).map {
        RedisStreamGroupInfo((it as List<Any?>).listOfPairsToMapAny() as Map<String, Any?>)
    }

/**
 * https://redis.io/commands/xinfo-consumers
 *
 * @since 5.0.0
 */
suspend fun Redis.xinfoConsumers(stream: String, group :String): List<RedisStreamConsumerInfo> =
    executeArrayAny("XINFO", "CONSUMERS", stream, group).map {
        RedisStreamConsumerInfo((it as List<Any?>).listOfPairsToMapAny() as Map<String, Any?>)
    }

/**
 * Patternized xreadgroup + xack processing
 */
suspend fun Redis.xprocessBatch(
    stream: String, group: String, consumer: String, id: String = ">", blockMs: Int = 1_000, batchSize: Int = 10,
    handler: suspend (String, String, Map<String, String>) -> Unit
) {
    val itemGrups = xreadgroup(stream to id, group = group, consumer = consumer, blockMs = blockMs, count = batchSize)
    for ((streamName, items) in itemGrups) {
        for ((id, item) in items) {
            try {
                handler(streamName, id, item)
                xack(streamName, group, id)
            } catch (e: Throwable) {
                // return it somehow?
                throw e
            }
        }
    }
}

/**
 * Patternized xreadgroup + xack processing infinite loop (until exception is thrown)
 */
suspend fun Redis.xprocessLoop(
    stream: String, group: String, consumer: String, id: String = ">", blockMs: Int = 1_000, batchSize: Int = 10,
    handler: suspend (String, String, Map<String, String>) -> Unit
) {
    // Until exception is thrown
    while (true) {
        xprocessBatch(stream, group, consumer, id, blockMs, batchSize, handler)
    }
}

/**
 * https://redis.io/commands/xgroup-help
 *
 * @since 5.0.0
 */
suspend fun Redis.xgroupHelp(): List<String> {
    return executeTyped<Any>("XGROUP", "HELP") as List<String>
}

/**
 * https://redis.io/commands/xgroup-create
 *
 * @since 5.0.0
 */
suspend fun Redis.xgroupCreate(stream: String, group: String, id: String = "\$"): Unit {
    return executeTyped("XGROUP", "CREATE", stream, group, id)
}

/**
 * https://redis.io/commands/xgroup-setid
 *
 * @since 5.0.0
 */
suspend fun Redis.xgroupSetId(stream: String, group: String, id: String = "\$"): Unit {
    return executeTyped("XGROUP", "SETID", stream, group, id)
}

/**
 * https://redis.io/commands/xgroup-destroy
 *
 * @since 5.0.0
 */
suspend fun Redis.xgroupDestroy(stream: String, group: String): Unit {
    return executeTyped("XGROUP", "DESTROY", stream, group)
}

/**
 * https://redis.io/commands/xgroup-delconsumer
 *
 * @since 5.0.0
 */
suspend fun Redis.xgroupDelConsumer(stream: String, group: String, consumer: String): Unit {
    return executeTyped("XGROUP", "DELCONSUMER", stream, group, consumer)
}

data class RedisStreamConsumerInfo(
    val map: Map<String, Any?>
) {
    val name: String get() = map["name"]?.toString() ?: "unknown"
    val pending: Long get() = (map["pending"] as Number).toLong()
    val idle: Long get() = (map["idle"] as Number).toLong()
}

data class RedisStreamGroupInfo(
    val map: Map<String, Any?>
) {
    val name: String get() = map["name"]?.toString() ?: "unknown"
    val consumers: Long get() = (map["consumers"] as Number).toLong()
    val pending: Long get() = (map["pending"] as Number).toLong()
}

data class RedisStreamInfo(
    val map: Map<String, Any?>
) {
    val length: Long get() = (map["length"] as Number).toLong()
    val groups: Long get() = (map["groups"] as Number).toLong()
    val radixTreeKeys: Long get() = (map["radix-tree-keys"] as Number).toLong()
    val radixTreeNodes: Long get() = (map["radix-tree-nodes"] as Number).toLong()
    val lastGeneratedId: String? get() = map["last-generated-id"] as? String?
    val firstEntry: Pair<String, Map<String, String>>? get() = map["first-entry"]?.let { xparseEntry(it as List<Any?>) }
    val lastEntry: Pair<String, Map<String, String>>? get() = map["last-entry"]?.let { xparseEntry(it as List<Any?>) }
}

private fun xparseListGroup(groups: List<Any?>): Map<String, Map<String, Map<String, String>>> {
    return groups.map {
        val p = it as List<Any?>
        val k = p[0] as String
        val v = xparseList((p[1] as List<Any?>))
        k to v
    }.toMap()
}

private fun xparseList(list: List<Any?>): Map<String, Map<String, String>> =
    list.map { xparseEntry(it as List<Any?>) }.toMap()

private fun xparseEntry(entry: List<Any?>): Pair<String, Map<String, String>> {
    val k = entry[0] as String
    val v = (entry[1] as List<String>).listOfPairsToMap()
    return k to v
}

private fun redisXnextId(id: String, incr: Int = +1): String {
    val (v0, v1) = id.split("-", limit = 2).map { it.toLong() }
    val nv1 = v1 + incr
    return when {
        nv1 < 0L -> "${v0 - 1}-0"
        else -> "$v0-$nv1"
    }
}

internal suspend fun Redis._xrange(args: List<Any?>): Map<String, Map<String, String>> {
    return xparseList(executeArrayAny(*(args.toTypedArray())))
}

internal suspend fun Redis._xrange(
    cmd: String, stream: String, first: String, second: String, count: Int? = null
): Map<String, Map<String, String>> =
    _xrange(arrayListOf<Any?>().apply {
        this += cmd
        this += stream
        this += first
        this += second
        if (count != null) {
            this += "COUNT"
            this += count
        }
    })
