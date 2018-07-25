package io.ktor.experimental.client.redis

suspend fun Redis.eval(script: String, vararg args: Pair<String, Any?>): Any? {
    return execute(
        "eval",
        script,
        args.size,
        *(args.map { it.first }.toTypedArray()),
        *(args.map { it.second }.toTypedArray())
    )
}

suspend fun Redis.eval(script: String, args: Map<String, Any?>) =
    eval(script, *(args.map { it.key to it.value }.toTypedArray()))

suspend fun Redis.evalsha(sha1: String, vararg args: Pair<String, Any?>): Any? {
    return execute(
        "evalsha",
        sha1,
        args.size,
        *(args.map { it.first }.toTypedArray()),
        *(args.map { it.second }.toTypedArray())
    )
}

suspend fun Redis.evalsha(sha1: String, args: Map<String, Any?>) =
    evalsha(sha1, *(args.map { it.key to it.value }.toTypedArray()))

suspend fun Redis.scriptLoad(script: String): String = this.commandString("SCRIPT", "LOAD", script) ?: ""

suspend fun Redis.scriptKill(): String = this.commandString("SCRIPT", "KILL") ?: ""

suspend fun Redis.scriptFlush(): String = this.commandString("SCRIPT", "FLUSH") ?: ""

enum class RedisScriptDebug { Yes, Sync, No }
suspend fun Redis.scriptDebug(kind: RedisScriptDebug): String = this.commandString("SCRIPT", "DEBUG", kind.name.toUpperCase()) ?: ""

suspend fun Redis.scriptExists(vararg sha1: String): List<Boolean> =
    this.commandArrayLong("SCRIPT", "EXISTS", *sha1).map { it != 0L }
