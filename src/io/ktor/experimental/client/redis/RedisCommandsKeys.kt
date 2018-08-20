package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*
import java.util.*

/**
 * Delete a key
 *
 * https://redis.io/commands/del
 *
 * @since 1.0.0
 */
suspend fun Redis.del(vararg keys: String) = commandString("del", *keys)

/**
 * This command is very similar to DEL: it removes the specified keys.
 * Just like DEL a key is ignored if it does not exist.
 * However the command performs the actual memory reclaiming in a different thread,
 * so it is not blocking, while DEL is.
 * This is where the command name comes from: the command just unlinks the keys from the keyspace.
 * The actual removal will happen later asynchronously.
 *
 * https://redis.io/commands/unlink
 *
 * @since 4.0.0
 */
suspend fun Redis.unlink(vararg keys: String) = commandString("unlink", *keys)

/**
 * Return a serialized version of the value stored at the specified key.
 *
 * https://redis.io/commands/dump
 *
 * @since 2.6.0
 */
suspend fun Redis.dump(key: String): ByteArray? = commandByteArray("dump", key)

/**
 * Create a key using the provided serialized value, previously obtained using DUMP.
 *
 * https://redis.io/commands/restore
 *
 * @since 2.6.0
 */
suspend fun Redis.restore(key: String, serializedValue: ByteArray?, ttl: Long = 0L, replace: Boolean = false): String {
    return commandString(
        "restore", key, ttl, serializedValue,
        *(if (replace) arrayOf("replace") else arrayOf())
    ) ?: ""
}

/**
 * Determine if a key exists
 *
 * https://redis.io/commands/exists
 *
 * @since 1.0.0
 */
suspend fun Redis.exists(key: String): Boolean = commandBool("exists", key)

/**
 * Determine if a key exists
 *
 * https://redis.io/commands/exists
 *
 * @since 1.0.0
 */
suspend fun Redis.exists(vararg keys: String): Long = commandLong("exists", *keys)

/**
 * Set a key's time to live in seconds
 *
 * https://redis.io/commands/expire
 *
 * @since 1.0.0
 */
suspend fun Redis.expire(key: String, time: Int) = commandString("expire", key, "$time")

/**
 * Set the expiration for a key as a UNIX timestamp
 *
 * https://redis.io/commands/expireat
 *
 * @since 1.2.0
 */
suspend fun Redis.expireat(key: String, date: Date) = commandString("expireat", key, "${date.time / 1000L}")

/**
 * Find all keys matching the given pattern
 *
 * https://redis.io/commands/keys
 *
 * @since 1.0.0
 */
suspend fun Redis.keys(pattern: String) = commandArrayString("keys", pattern)

/**
 * Atomically transfer a key from a Redis instance to another one.
 *
 * https://redis.io/commands/migrate
 *
 * @since 2.6.0
 */
internal suspend fun Redis.migrate(todo: Any?): Unit = TODO()

/**
 * Move a key to another database
 *
 * https://redis.io/commands/move
 *
 * @since 1.0.0
 */
suspend fun Redis.move(key: String, db: Int) = commandString("move", key, db)

/**
 * Returns the number of references of the value associated with the specified key.
 * This command is mainly useful for debugging.
 *
 * https://redis.io/commands/object
 *
 * @since 2.2.3
 */
suspend fun Redis.objectRefcount(key: String) = commandLong("object", "refcount", key)

/**
 * Returns the kind of internal representation used in order to store the value associated with a key.
 *
 * https://redis.io/commands/object
 *
 * @since 2.2.3
 */
suspend fun Redis.objectEncoding(key: String) = commandString("object", "encoding", key)

/**
 * Returns the number of seconds since the object stored at the specified key is idle
 * (not requested by read or write operations).
 * While the value is returned in seconds the actual resolution of this timer is 10 seconds,
 * but may vary in future implementations.
 * This subcommand is available when maxmemory-policy is set to an LRU policy or noeviction.
 *
 * https://redis.io/commands/object
 *
 * @since 2.2.3
 */
suspend fun Redis.objectIdletime(key: String) = commandLong("object", "idletime", key)

/**
 * Returns the logarithmic access frequency counter of the object stored at the specified key.
 * This subcommand is available when maxmemory-policy is set to an LFU policy.
 *
 * https://redis.io/commands/object
 *
 * @since 2.2.3
 */
suspend fun Redis.objectFreq(key: String) = commandLong("object", "freq", key)

/**
 * Returns a succint help text.
 *
 * https://redis.io/commands/object
 *
 * @since 2.2.3
 */
suspend fun Redis.objectHelp() = commandString("object", "help")

/**
 * Remove the existing timeout on key, turning the key from volatile (a key with an expire set)
 * to persistent (a key that will never expire as no timeout is associated).
 *
 * https://redis.io/commands/persist
 *
 * @since 2.2.0
 */
suspend fun Redis.persist(key: String) = commandString("persist", key)

/**
 * This command works exactly like EXPIRE but the time to live
 * of the key is specified in milliseconds instead of seconds.
 *
 * https://redis.io/commands/pexpire
 *
 * @since 2.6.0
 */
suspend fun Redis.pexpire(key: String, ms: Long) = commandString("pexpire", key, ms)

/**
 * PEXPIREAT has the same effect and semantic as EXPIREAT,
 * but the Unix time at which the key will expire is specified in milliseconds instead of seconds.
 *
 * https://redis.io/commands/pexpireat
 *
 * @since 2.6.0
 */
suspend fun Redis.pexpireat(key: String, date: Date) = commandString("pexpireat", key, "${date.time}")

/**
 * This commands returns the remaining time in milliseconds to live of a key that has an expire set.
 *
 * A values less than 0, means an error.
 *
 * https://redis.io/commands/pttl
 *
 * @since 2.6.0
 */
suspend fun Redis.pttl(key: String): Long = commandLong("pttl", key)

/**
 * This commands returns the remaining time in seconds to live of a key that has an expire set.
 *
 * A values less than 0, means an error.
 *
 * https://redis.io/commands/ttl
 *
 * @since 1.0.0
 */
suspend fun Redis.ttl(key: String): Long = commandLong("ttl", key)

/**
 * Return a random key from the currently selected database.
 *
 * https://redis.io/commands/randomkey
 *
 * @since 1.0.0
 */
suspend fun Redis.randomkey(): String? = commandString("randomkey")

/**
 * Renames oldKey to newKey.
 * - If oldKey doesn't exists, it returns an error.
 * - If newkey already exists, it is overwritten.
 *
 * https://redis.io/commands/rename
 *
 * @since 1.0.0
 */
suspend fun Redis.rename(oldKey: String, newKey: String) = commandUnit("rename", oldKey, newKey)

/**
 * Renames oldKey to newKey.
 * - If oldKey doesn't exists, it returns an error.
 * - If newkey already exists, it is NOT overwritten, and the function returns false.
 *
 * https://redis.io/commands/renamenx
 *
 * @since 1.0.0
 */
suspend fun Redis.renamenx(oldKey: String, newKey: String) = commandBool("renamenx", oldKey, newKey)

/**
 * Incrementally iterate the keys space
 *
 * https://redis.io/commands/scan
 *
 * @since 2.8.0
 */
internal suspend fun Redis.scan(pattern: String? = null): ReceiveChannel<String> = scanBaseString("scan", null, pattern)

/**
 * Returns or stores the elements contained in the list, set or sorted set at key.
 * By default, sorting is numeric and elements are compared by their value interpreted as double precision
 * floating point number. This is SORT in its simplest form: SORT mylist
 *
 * https://redis.io/commands/sort
 *
 * @since 1.0.0
 */
internal suspend fun Redis.sort(todo: Any): Unit = TODO()

/**
 * Alters the last access time of a key(s). A key is ignored if it does not exist.
 *
 * https://redis.io/commands/touch
 *
 * @since 3.2.1
 */
suspend fun Redis.touch(vararg keys: String): Long = commandLong("touch", *keys)

/**
 * Returns the string representation of the type of the value stored at key.
 * The different types that can be returned are: string, list, set, zset and hash.
 *
 * https://redis.io/commands/type
 *
 * @since 1.0.0
 */
suspend fun Redis.type(key: String): String? = commandString("type", key)

/**
 * This command blocks the current client until all the previous write commands are successfully
 * transferred and acknowledged by at least the specified number of slaves.
 * If the timeout, specified in milliseconds, is reached, the command returns even
 * if the specified number of slaves were not yet reached.
 *
 * https://redis.io/commands/wait
 *
 * @since 3.0.0
 */
suspend fun Redis.wait(numslaves: Int, timeoutMs: Int = 0): String? = commandString("wait", numslaves, timeoutMs)
