package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*
import java.util.*

/**
 * Asynchronously rewrite the append-only file
 *
 * https://redis.io/commands/bgrewriteaof
 *
 * @since 1.0.0
 */
suspend fun Redis.bgrewriteaof(): String? = executeTypedNull<String>("bgrewriteaof")

/**
 * Asynchronously save the dataset to disk
 *
 * https://redis.io/commands/bgsave
 *
 * @since 1.0.0
 */
suspend fun Redis.bgsave(): String? = executeTypedNull<String>("bgsave")

/**
 * Get the current connection name.
 *
 * https://redis.io/commands/client-getname
 *
 * @since 2.6.9
 */
suspend fun Redis.clientGetname(): String? = executeTypedNull<String>("client", "getname")

/**
 * Kill the connection of a client
 *
 * https://redis.io/commands/client-kill
 *
 * @param type Possible values: normal, master, slave or pubsub
 *
 * @since 2.4.0
 */
//[ip:port] [ID client-id] [TYPE normal|master|slave|pubsub] [ADDR ip:port] [SKIPME yes/no]
suspend fun Redis.clientKill(clientId: String? = null, type: String? = null, addr: String? = null, skipme: Boolean = true): Unit =
    executeBuildNotNull {
        if (clientId != null) {
            add("ID")
            add(clientId)
        }
        if (type != null) {
            add("TYPE")
            add(type)
        }
        if (addr != null) {
            add("ADDR")
            add(addr)
        }
        if (!skipme) {
            add("SKIPME")
            add(if (skipme) "yes" else "no")
        }
    }

/**
 * Get the list of client connections
 *
 * https://redis.io/commands/client-list
 *
 * @since 2.4.0
 */
suspend fun Redis.clientList(): List<Map<String, String>> {
    val res = executeTypedNull<String>("client", "list") ?: ""
    return res.lines().map {
        it.split(' ').map {
            val parts = it.split('=', limit = 2)
            parts[0] to parts.getOrElse(1) { "" }
        }.toMap()
    }
}

/**
 * Stop processing commands from clients for some time.
 *
 * https://redis.io/commands/client-pause
 *
 * @since 2.9.50
 */
suspend fun Redis.clientPause(timeoutMs: Int): Unit = executeTyped("client", "pause", timeoutMs)


/**
 * Instruct the server whether to reply to commands.
 *
 * https://redis.io/commands/client-reply
 *
 * @since 3.2
 */
private suspend fun Redis.clientReply(mode: RedisClientReplyMode): Unit =
    executeTyped<Unit>("client", "reply", mode.name)

suspend fun Redis.clientReplyOn(): Unit = clientReply(RedisClientReplyMode.ON)
suspend fun Redis.clientReplyOff(): Unit = clientReply(RedisClientReplyMode.OFF)
suspend fun Redis.clientReplySkip(): Unit = clientReply(RedisClientReplyMode.SKIP)

internal suspend inline fun Redis.clientReplyOff(callback: () -> Unit) {
    clientReplyOff()
    try {
        callback()
    } finally {
        clientReplyOn()
    }
}

/**
 * Set the current connection name.
 *
 * https://redis.io/commands/client-setname
 *
 * @since 2.6.9
 */
suspend fun Redis.clientSetname(name: String): Unit = executeTyped("client", "setname", name)

data class CommandInfo(
    /** command name */
    val name: String,

    /** command arity specification */
    val arity: Int,

    /** nested Array reply of command flags */
    val flags: Set<String>,

    /** position of first key in argument list */
    val firstKey: Int,

    /** position of last key in argument list */
    val lastKey: Int,

    /** step count for locating repeating keys */
    val step: Int
) {
    /** command may result in modifications */
    val hasWrite get() = "write" in flags

    /** command will never modify keys */
    val hasReadonly get() = "readonly" in flags

    /** reject command if currently OOM */
    val hasDenyoom get() = "denyoom" in flags

    /** server admin command */
    val hasAdmin get() = "admin" in flags

    /** pubsub-related command */
    val hasPubsub get() = "pubsub" in flags

    /** deny this command from scripts */
    val hasNoscript get() = "noscript" in flags

    /** command has random results, dangerous for scripts */
    val hasRandom get() = "random" in flags

    /** if called from script, sort output */
    val hasSortForScripts get() = "sort_for_script" in flags

    /** allow command while database is loading */
    val hasLoading get() = "loading" in flags

    /** allow command while replica has stale data */
    val hasStale get() = "stale" in flags

    /** do not show this command in MONITOR */
    val hasSkipMonitor get() = "skip_monitor" in flags

    /** cluster related - accept even if importing */
    val hasAsking get() = "asking" in flags

    /** command operates in constant or log(N) time. Used for latency monitoring. */
    val hasFast get() = "fast" in flags

    /** keys have no pre-determined position. You must discover keys yourself. */
    val hasMoveableKeys get() = "movablekeys" in flags
}

private suspend fun Redis._commandInfo(vararg args: String): List<CommandInfo> = executeArrayAny(*args).map {
    val parts = it as List<Any>
    CommandInfo(
        name = parts[0].toString(),
        arity = (parts[1] as Long).toInt(),
        flags = (parts[2] as List<String>).toSet(),
        firstKey = (parts[3] as Long).toInt(),
        lastKey = (parts[4] as Long).toInt(),
        step = (parts[5] as Long).toInt()
    )
}

/**
 * Get array of Redis command details
 *
 * https://redis.io/commands/command
 *
 * @since 2.8.13
 */
@Suppress("UNCHECKED_CAST")
suspend fun Redis.command(): List<CommandInfo> = _commandInfo("command")

/**
 * Get array of specific Redis command details
 *
 * https://redis.io/commands/command-info
 *
 * @since 2.8.13
 */
suspend fun Redis.commandInfo(vararg names: String): List<CommandInfo> =
    _commandInfo("command", "info", *names)

/**
 * Get total number of Redis commands.
 *
 * https://redis.io/commands/command-count
 *
 * @since 2.8.13
 */
suspend fun Redis.commandCount(): Int = executeTyped("command", "count")

/**
 * Extract keys given a full Redis command
 *
 * https://redis.io/commands/command-getkeys
 *
 * @since 2.8.13
 */
suspend fun Redis.commandGetKeys(vararg args: Any?): List<String> =
    executeArrayString("command", "getkeys", *args)

/**
 * Get the value of a configuration parameter
 *
 * https://redis.io/commands/config-get
 *
 * @since 2.0.0
 */
suspend fun Redis.configGet(pattern: String = "*"): Map<String, String> {
    return executeArrayString("config", "get", pattern).toListOfPairsString().toMap()
}

/**
 * Reset the stats returned by INFO.
 *
 * https://redis.io/commands/config-resetstat
 *
 * @since 2.0.0
 */
suspend fun Redis.configResetStat(): Unit = executeTyped("config", "resetstat")

/**
 * Rewrite the configuration file with the in memory configuration.
 *
 * https://redis.io/commands/config-rewrite
 *
 * @since 2.8.0
 */
suspend fun Redis.configRewrite(): Unit = executeTyped("config", "rewrite")

/**
 * Set a configuration parameter to the given value.
 *
 * https://redis.io/commands/config-set
 *
 * @since 2.0.0
 */
suspend fun Redis.configSet(key: String, value: Any?): Unit = executeTyped("config", "set", key, value)

/**
 * Return the number of keys in the selected database.
 *
 * https://redis.io/commands/dbsize
 *
 * @since 1.0.0
 */
suspend fun Redis.dbsize(): Long = executeTyped("dbsize")

/**
 * Get debugging information about a key.
 *
 * https://redis.io/commands/debug-object
 *
 * @since 1.0.0
 */
suspend fun Redis.debugObject(key: String): String? = executeTypedNull<String>("debug", "object", key)

/**
 * Make the server crash.
 *
 * https://redis.io/commands/debug-segfault
 *
 * @since 1.0.0
 */
suspend fun Redis.debugSegfault(): String? = executeTypedNull<String>("debug", "segfault")

/**
 * Remove all keys from all databases.
 *
 * https://redis.io/commands/flushall
 *
 * @since 1.0.0
 * @since 4.0.0 (async)
 */
suspend fun Redis.flushall(async: Boolean = false) =
    executeTyped<Unit>("flushall", *arrayOfNotNull(if (async) "async" else null))

/**
 * Remove all keys from the current database.
 *
 * https://redis.io/commands/flushdb
 *
 * @since 1.0.0
 * @since 4.0.0 (async)
 */
suspend fun Redis.flushdb(async: Boolean = false) =
    executeTyped<Unit>("flushdb", *arrayOfNotNull(if (async) "async" else null))

/**
 * Get information and statistics about the server.
 *
 * https://redis.io/commands/info
 *
 * @since 1.0.0
 */
suspend fun Redis.info(section: String? = null) = executeTypedNull<String>("info", *arrayOfNotNull(section))

/**
 * Get the UNIX time stamp of the last successful save to disk.
 *
 * https://redis.io/commands/lastsave
 *
 * @since 1.0.0
 */
suspend fun Redis.lastsave() = Date(executeTyped<Long>("lastsave") * 1000L)

/**
 * Reports about different memory-related issues that the Redis server experiences,
 * and advises about possible remedies.
 *
 * https://redis.io/commands/memory-doctor
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryDoctor() = executeTypedNull<String>("memory", "doctor")

/**
 * Returns a helpful text describing the different subcommands.
 *
 * https://redis.io/commands/memory-help
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryHelp() = executeArrayString("memory", "help")

/**
 * Provides an internal statistics report from the memory allocator.
 *
 * This command is currently implemented only when using jemalloc as an allocator,
 * and evaluates to a benign NOOP for all others.
 *
 * https://redis.io/commands/memory-malloc-stats
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryMallocStats() = executeArrayString("memory", "malloc-stats")

/**
 * Attempts to purge dirty pages so these can be reclaimed by the allocator.
 *
 * This command is currently implemented only when using jemalloc as an allocator,
 * and evaluates to a benign NOOP for all others.
 *
 * https://redis.io/commands/memory-purge
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryPurge() = executeTypedNull<String>("memory", "purge")

/**
 * Returns an Array reply about the memory usage of the server.
 *
 * https://redis.io/commands/memory-stats
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryStats(): Any? = executeText("memory", "stats")

/**
 * Reports the number of bytes that a key and its value require to be stored in RAM.
 *
 * https://redis.io/commands/memory-usage
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryUsage(key: String, samplesCount: Long? = null) =
    executeTyped<Long>(
        "memory",
        "usage",
        key,
        *(if (samplesCount != null) arrayOf("samples", samplesCount) else arrayOf())
    )

// @TODO: This changes the client state too
/**
 * Streams back every command processed by the Redis server.
 * It can help in understanding what is happening to the database.
 * This command can both be used via redis-cli and via telnet.
 *
 * https://redis.io/commands/monitor
 *
 * @since 1.0.0
 */
suspend inline fun Redis.monitor(): ReceiveChannel<String> {
    executeTyped<Unit>("MONITOR")
    val stream = RedisInternalChannel.run { getMessageChannel() }
    return stream.map { it.toString() }
}

/**
 * Provide information on the role of a Redis instance in the context of replication,
 * by returning if the instance is currently a master, slave, or sentinel.
 * The command also returns additional information about the state of the replication (if the role is master or slave)
 * or the list of monitored master names (if the role is sentinel).
 *
 *
 * https://redis.io/commands/role
 *
 * @since 2.8.12
 */
suspend fun Redis.role(): Any? = executeText("ROLE")

/**
 * Performs a synchronous save of the dataset producing a point in time snapshot of all the data inside
 * the Redis instance, in the form of an RDB file.
 *
 * https://redis.io/commands/save
 *
 * @since 1.0.0
 */
suspend fun Redis.save(): Long = executeTyped("SAVE")

/**
 * Internal command used for replication
 *
 * https://redis.io/commands/sync
 *
 * @since 1.0.0
 */
suspend fun Redis.sync(): Unit = executeTyped("SYNC")

/**
 * Returns the current server time as a two items lists: a Unix timestamp and the
 * amount of microseconds already elapsed in the current second.
 * Basically the interface is very similar to the one of the gettimeofday system call.
 *
 * https://redis.io/commands/time
 *
 * @since 2.6.0
 */
suspend fun Redis.time(): Pair<Date, Long> {
    val res = executeArrayString("TIME")
    val unixSeconds = res.getOrElse(0) { "0" }.toLong()
    val microSeconds = res.getOrElse(1) { "0" }.toLong()
    return Date(unixSeconds * 1000L + (microSeconds / 1000L)) to (microSeconds % 1000L)
}

/**
 * Synchronously save the dataset to disk and then shut down the server.
 *
 * https://redis.io/commands/shutdown
 *
 * @since 1.0.0
 */
suspend fun Redis.shutdown(save: Boolean = true): Unit = executeTyped("SHUTDOWN", if (save) "SAVE" else "NOSAVE")

/**
 * Make the server a slave of another instance, or promote it as master.
 *
 * https://redis.io/commands/slaveof
 *
 * @since 1.0.0
 */
suspend fun Redis.slaveof(host: String, port: Int): Unit = executeTyped("SLAVEOF", host, port)

/**
 * Manages the Redis slow queries log.
 *
 * https://redis.io/commands/slowlog
 *
 * @since 2.2.12
 */
suspend fun Redis.slowlog(subcommand: String, vararg args: Any?): Any? = executeText("SLOWLOG", subcommand, *args)
