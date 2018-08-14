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
suspend fun Redis.bgrewriteaof(): String? = commandString("bgrewriteaof")

/**
 * Asynchronously save the dataset to disk
 *
 * https://redis.io/commands/bgsave
 *
 * @since 1.0.0
 */
suspend fun Redis.bgsave(): String? = commandString("bgsave")

suspend fun Redis.clientGetname(): String? = commandString("client", "getname")

internal suspend fun Redis.clientKill(todo: Any): Any = TODO()

suspend fun Redis.clientList(): List<Map<String, String>> {
    val res = commandString("client", "list") ?: ""
    return res.lines().map {
        it.split(' ').map {
            val parts = it.split('=', limit = 2)
            parts[0] to parts.getOrElse(1) { "" }
        }.toMap()
    }
}

suspend fun Redis.clientPause(timeoutMs: Int): Unit = commandUnit("client", "pause", timeoutMs)

enum class RedisClientReplyMode { ON, OFF, SKIP }

// @TODO: This would require some processing from the client.
suspend fun Redis.clientReply(mode: RedisClientReplyMode): Unit = commandUnit("client", "reply", mode.name)

suspend fun Redis.clientSetname(name: String): Unit = commandUnit("client", "setname", name)

suspend fun Redis.command(): Any = executeText("command")!!
suspend fun Redis.commandCount(): Long = commandLong("command", "count")
internal suspend fun Redis.commandGetKeys(todo: Any): Any = TODO()

internal suspend fun Redis.commandInfo(vararg names: String): Any = executeText("command", "info", *names)!!

internal suspend fun Redis.configGet(pattern: String): Map<String, String> {
    return commandArrayString("config", "get", pattern).toListOfPairs().toMap()
}

suspend fun Redis.configResetStat(): Unit = commandUnit("config", "resetstat")

suspend fun Redis.configRewrite(): Unit = commandUnit("config", "rewrite")

suspend fun Redis.configSet(key: String, value: Any?): Unit = commandUnit("config", "set", key, value)

suspend fun Redis.dbsize(): Long = commandLong("dbsize")

suspend fun Redis.debugObject(key: String): String? = commandString("debug", "object", key)

suspend fun Redis.debugSegfault(): String? = commandString("debug", "segfault")

suspend fun Redis.flushall(async: Boolean = false) =
    commandUnit("flushall", *arrayOfNotNull(if (async) "async" else null))

suspend fun Redis.flushdb(async: Boolean = false) =
    commandUnit("flushdb", *arrayOfNotNull(if (async) "async" else null))

suspend fun Redis.info(section: String? = null) = commandString("info", *arrayOfNotNull(section))

suspend fun Redis.lastsave() = Date(commandLong("lastsave") * 1000L)

suspend fun Redis.memoryDoctor() = commandString("memory", "doctor")

suspend fun Redis.memoryList() = commandArrayString("memory", "list")

suspend fun Redis.memoryMallocStats() = commandArrayString("memory", "malloc-stats")

suspend fun Redis.memoryPurge() = commandString("memory", "purge")

suspend fun Redis.memoryStats() = executeText("memory", "stats")

suspend fun Redis.memoryUsage(key: String, samplesCount: Long? = null) =
    commandLong("memory", "usage", key, *(if (samplesCount != null) arrayOf("samples", samplesCount) else arrayOf()))

// @TODO: This changes the client state too
internal suspend fun Redis.monitor(): Channel<String> = TODO()

suspend fun Redis.role(): Any? = executeText("role")

suspend fun Redis.save(): Long = commandLong("save")

suspend fun Redis.sync(): Unit = commandUnit("sync")

suspend fun Redis.time(): Pair<Long, Long> {
    val res = commandArrayLong("time")
    return res.getOrElse(0) { 0 } to res.getOrElse(1) { 0 }
}

suspend fun Redis.shutdown(save: Boolean = true): Unit = commandUnit("shutdown", if (save) "save" else "nosave")

suspend fun Redis.slaveof(host: String, port: Int): Unit = commandUnit("slaveof", host, port)

suspend fun Redis.slowlog(subcommand: String, vararg args: Any?): Any? = executeText("slowlog", subcommand, *args)
