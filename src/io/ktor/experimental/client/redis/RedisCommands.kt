package io.ktor.experimental.client.redis

// @TODO: Missing commands & generate

suspend fun Redis.ping(): String? = commandString("ping")

suspend fun Redis.append(key: String, value: String): String? = commandString("append", key, value)
suspend fun Redis.bgrewriteaof(): String? = commandString("bgrewriteaof")
suspend fun Redis.bgsave(): String? = commandString("bgsave")
suspend fun Redis.bitcount(key: String): String? = commandString("bitcount", key)
suspend fun Redis.bitcount(key: String, start: Int, end: Int): String? =
    commandString("bitcount", key, "$start", "$end")

suspend fun Redis.exists(key: String): Boolean = commandBool("exists", key)
suspend fun Redis.exists(vararg keys: String): Long = commandLong("exists", *keys)
suspend fun Redis.set(key: String, value: String): String? = commandString("set", key, value)
suspend fun Redis.get(key: String): String? = commandString("get", key)
suspend fun Redis.del(vararg keys: String) = commandString("del", *keys)
suspend fun Redis.echo(msg: String) = commandString("echo", msg)
suspend fun Redis.expire(key: String, time: Int) = commandString("expire", key, "$time")

@Suppress("UNCHECKED_CAST")
suspend fun Redis.commandArrayString(vararg args: Any?): List<String> =
    (execute(*args) as List<Any?>?)?.map { it.toString() } ?: listOf() // toString required because, it returns a CharBuffer

suspend fun Redis.commandArrayStringNotNull(vararg args: Any?): List<String> =
    (execute(*args) as List<Any?>?)?.filterNotNull()?.map { it.toString() } ?: listOf() // toString required because, it returns a CharBuffer

@Suppress("UNCHECKED_CAST")
suspend fun Redis.commandArrayLong(vararg args: Any?): List<Long> =
    (execute(*args) as List<Long>?) ?: listOf()

suspend fun Redis.commandString(vararg args: Any?): String? = execute(*args)?.toString()
suspend fun Redis.commandLong(vararg args: Any?): Long = execute(*args)?.toString()?.toLongOrNull() ?: 0L
suspend fun Redis.commandDouble(vararg args: Any?): Double = execute(*args)?.toString()?.toDoubleOrNull() ?: 0.0
suspend fun Redis.commandUnit(vararg args: Any?): Unit = run { execute(*args) }
suspend fun Redis.commandBool(vararg args: Any?): Boolean = commandLong(*args) != 0L

internal fun <T> List<T>.toListOfPairs(): List<Pair<String, String>> =
    (0 until size / 2).map { ("" + this[it * 2 + 0]) to ("" + this[it * 2 + 1]) }

internal fun List<Any?>.listOfPairsToMap(): Map<String, String> =
    (0 until size / 2).map { ("" + this[it * 2 + 0]) to ("" + this[it * 2 + 1]) }.toMap()
