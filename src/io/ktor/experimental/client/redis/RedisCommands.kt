package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*

enum class SortDirection { ASC, DESC }

suspend fun Redis.executeBinary(vararg args: Any?): Any? = execute(*args)
suspend fun Redis.executeText(vararg args: Any?): Any? = execute(*args).byteArraysToString

@Suppress("UNCHECKED_CAST")
suspend fun Redis.commandArrayString(vararg args: Any?): List<String> =
    (executeText(*args) as List<Any?>?)?.filterIsInstance<String>() ?: listOf()

suspend fun Redis.commandArrayAny(vararg args: Any?): List<Any?> =
    (executeText(*args) as List<Any?>?) ?: listOf()

suspend fun Redis.commandArrayStringNull(vararg args: Any?): List<String?> =
    (executeText(*args) as List<Any?>?) as? List<String?> ?: listOf()

@Suppress("UNCHECKED_CAST")
suspend fun Redis.commandArrayLong(vararg args: Any?): List<Long> =
    (executeText(*args) as List<Long>?) ?: listOf()

suspend fun Redis.commandLong(vararg args: Any?): Long = commandAny(*args) ?: 0L
suspend fun Redis.commandInt(vararg args: Any?): Int = commandAny(*args) ?: 0

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandByteArray(vararg args: Any?): ByteArray? = commandAny(*args)

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandDoubleOrNull(vararg args: Any?): Double? = commandAny(*args)

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandDouble(vararg args: Any?): Double = commandAnyNotNull(*args)

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandString(vararg args: Any?): String? = commandAny(*args)

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandUnit(vararg args: Any?): Unit = commandAnyNotNull(*args)

@Deprecated("", ReplaceWith("command(*args)"))
suspend fun Redis.commandBool(vararg args: Any?): Boolean = commandAnyNotNull(*args)

suspend inline fun <reified T> Redis.commandAny(vararg args: Any?): T? = when (T::class) {
    Boolean::class -> (commandLong(*args) != 0L) as T?
    Unit::class -> run { executeText(*args); Unit as T? }
    Any::class -> run { executeText(*args) as T? }
    String::class -> executeText(*args)?.toString() as T?
    Double::class -> executeText(*args)?.toString()?.toDoubleOrNull() as T?
    Int::class -> executeText(*args)?.toString()?.toIntOrNull() as T?
    Long::class -> executeText(*args)?.toString()?.toLongOrNull() as T?
    ByteArray::class -> execute(*args) as? T?
    else -> error("Unsupported type")
}

suspend inline fun <reified T> Redis.commandBuild(
    initialCapacity: Int = 16, callback: ArrayList<Any?>.() -> Unit
): T? = commandAny(*ArrayList<Any?>(initialCapacity).apply(callback).toTypedArray())

suspend inline fun <reified T> Redis.commandBuildNotNull(
    initialCapacity: Int = 16, callback: ArrayList<Any?>.() -> Unit
): T = commandAnyNotNull(*ArrayList<Any?>(initialCapacity).apply(callback).toTypedArray())

suspend inline fun <reified T> Redis.commandAnyNotNull(vararg args: Any?): T = commandAny(*args)!!

internal fun <T> List<T>.toListOfPairs(): List<Pair<T, T>> =
    (0 until size / 2).map { this[it * 2 + 0] to this[it * 2 + 1] }

internal fun <T> List<T>.toListOfPairsString(): List<Pair<String, String>> =
    (0 until size / 2).map { ("${this[it * 2 + 0]}") to ("${this[it * 2 + 1]}") }

internal fun List<Any?>.listOfPairsToMap(): Map<String, String> =
    (0 until size / 2).map { ("${this[it * 2 + 0]}") to ("${this[it * 2 + 1]}") }.toMap()

internal fun List<Any?>.listOfPairsToMapAny(): Map<Any?, Any?> =
    (0 until size / 2).map { this[it * 2 + 0] to this[it * 2 + 1] }.toMap()

private val UTF8 = Charsets.UTF_8

private val Any?.byteArraysToString: Any?
    get() = when (this) {
        is ByteArray -> this.toString(UTF8)
        is List<*> -> { // @TODO: Copy only on different instances
            this.map { it.byteArraysToString }.toList()
        }
        is Map<*, *> -> { // @TODO: Copy only on different instances
            this.map { it.key.byteArraysToString to it.value.byteArraysToString }.toMap()
        }
        else -> this
    }

internal inline fun <reified T : Any> arrayOfNotNull(vararg items: T?): Array<T> = listOfNotNull(*items).toTypedArray()

data class RedisScanStepResult(val nextCursor: Long, val items: List<String>)

internal suspend fun Redis._scanBaseStep(
    cmd: String,
    key: String?,
    cursor: Long,
    pattern: String? = null,
    count: Int? = null
): RedisScanStepResult {
    val result = commandArrayAny(*arrayListOf<Any?>().apply {
        this += cmd
        if (key != null) {
            this += key
        }
        this += cursor
        if (pattern != null) {
            this += "PATTERN"
            this += pattern
        }
        if (count != null) {
            this += "COUNT"
            this += count
        }
    }.toTypedArray())
    return RedisScanStepResult(result[0].toString().toLong(), result[1] as List<String>)
}

internal suspend fun Redis._scanBase(
    cmd: String, key: String?,
    pattern: String? = null, count: Int? = null,
    pairs: Boolean = false
): ReceiveChannel<Any> = Channel<Any>((count ?: 10) * 2)._sending {
    var cursor = 0L
    do {
        val result = _scanBaseStep(cmd, key, cursor, pattern, count)
        cursor = result.nextCursor
        val items = result.items
        if (pairs) {
            for (n in 0 until items.size step 2) {
                send(items[n + 0] to items[n + 1])
            }
        } else {
            for (item in items) {
                send(item)
            }
        }
    } while (cursor > 0L)
}
i
internal fun <T> Channel<T>._sending(callback: suspend SendChannel<T>.() -> Unit): ReceiveChannel<T> {
    launch(start = CoroutineStart.UNDISPATCHED) {
        try {
            callback()
        } finally {
            close()
        }
    }
    return this
}

internal suspend fun Redis._scanBaseString(
    cmd: String, key: String?, pattern: String? = null, count: Int? = null
): ReceiveChannel<String> = _scanBase(cmd, key, pattern, count, pairs = false) as Channel<String>

internal suspend fun Redis._scanBasePairs(
    cmd: String, key: String?, pattern: String? = null, count: Int? = null
): ReceiveChannel<Pair<String, String>> =
    _scanBase(cmd, key, pattern, count, pairs = true) as Channel<Pair<String, String>>
