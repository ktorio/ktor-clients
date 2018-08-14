package io.ktor.experimental.client.redis

/**
 * Discard all commands issued after MULTI
 *
 * https://redis.io/commands/discard
 *
 * @since 2.0.0
 */
suspend fun Redis.discard() = commandUnit("discard")

/**
 * Execute all commands issued after MULTI
 *
 * https://redis.io/commands/exec
 *
 * @since 1.2.0
 */
suspend fun Redis.exec() = commandUnit("exec")

/**
 * Mark the start of a transaction block
 *
 * https://redis.io/commands/multi
 *
 * @since 1.2.0
 */
suspend fun Redis.multi() = commandUnit("multi")

/**
 * Forget about all watched keys
 *
 * https://redis.io/commands/unwatch
 *
 * @since 2.2.0
 */
suspend fun Redis.unwatch() = commandUnit("unwatch")

/**
 * Watch the given keys to determine execution of the MULTI/EXEC block
 *
 * https://redis.io/commands/watch
 *
 * @since 2.2.0
 */
suspend fun Redis.watch(vararg keys: String) = commandUnit("watch", *keys)

/**
 * Executes a transaction.
 * - If no exception is thrown, all the commands executed inside callback will be commited
 * - If an exception is thrown, all the commands executed inside callback will be discarded and the exception rethrown
 *
 * @since 2.2.0
 */
suspend inline fun Redis.transaction(vararg keys: String, callback: () -> Unit) {
    multi()
    if (keys.isNotEmpty()) watch(*keys)
    try {
        callback()
        exec()
    } catch (e: Throwable) {
        discard()
        throw e
    }
}
