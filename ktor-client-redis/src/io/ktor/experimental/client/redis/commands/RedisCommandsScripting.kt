package io.ktor.experimental.client.redis

/**
 * Execute a Lua script server side.
 *
 * https://redis.io/commands/eval
 *
 * @since 2.6.0
 */
suspend fun Redis.eval(script: String, vararg args: Pair<String, Any?>): Any? = executeText(
    "EVAL", script, args.size,
    *(args.map { it.first }.toTypedArray()),
    *(args.map { it.second }.toTypedArray())
)

/**
 * Execute a Lua script server side.
 *
 * https://redis.io/commands/eval
 *
 * @since 2.6.0
 */
suspend fun Redis.eval(script: String, args: Map<String, Any?>): Any? =
    eval(script, *(args.map { it.key to it.value }.toTypedArray()))

/**
 * Execute a Lua script server side.
 *
 * https://redis.io/commands/evalsha
 *
 * @since 2.6.0
 */
suspend fun Redis.evalsha(sha1: String, vararg args: Pair<String, Any?>): Any? = executeText(
    "EVALSHA", sha1, args.size,
    *(args.map { it.first }.toTypedArray()),
    *(args.map { it.second }.toTypedArray())
)

/**
 * Execute a Lua script server side.
 *
 * https://redis.io/commands/evalsha
 *
 * @since 2.6.0
 */
suspend fun Redis.evalsha(sha1: String, args: Map<String, Any?>): Any? =
    evalsha(sha1, *(args.map { it.key to it.value }.toTypedArray()))

/**
 * Load the specified Lua script into the script cache.
 *
 * https://redis.io/commands/script-load
 *
 * @since 2.6.0
 */
suspend fun Redis.scriptLoad(script: String): String = executeTypedNull("SCRIPT", "LOAD", script) ?: ""

/**
 * Kill the script currently in execution.
 *
 * https://redis.io/commands/script-kill
 *
 * @since 2.6.0
 */
suspend fun Redis.scriptKill(): String = executeTypedNull("SCRIPT", "KILL") ?: ""

/**
 * Remove all the scripts from the script cache.
 *
 * https://redis.io/commands/script-flush
 *
 * @since 2.6.0
 */
suspend fun Redis.scriptFlush(): String = executeTypedNull("SCRIPT", "FLUSH") ?: ""

enum class RedisScriptDebug { Yes, Sync, No }

/**
 * Set the debug mode for executed scripts.
 *
 * https://redis.io/commands/script-debug
 *
 * @since 3.2.0
 */
suspend fun Redis.scriptDebug(kind: RedisScriptDebug): String =
    executeTypedNull("SCRIPT", "DEBUG", kind.name.toUpperCase()) ?: ""

/**
 * Check existence of scripts in the script cache.
 *
 * https://redis.io/commands/script-exists
 *
 * @since 2.6.0
 */
suspend fun Redis.scriptExists(vararg sha1: String): List<Boolean> =
    executeArrayLong("SCRIPT", "EXISTS", *sha1).map { it != 0L }
