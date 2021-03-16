package io.ktor.experimental.client.redis

import io.ktor.http.*
import io.ktor.util.*

@InternalAPI
object Generate {
    @JvmStatic
    fun main(args: Array<String>) {
        RedisCommands.apply {
            for (cmd in commands) {
                val params = cmd.toKotlinParams()
                println("suspend fun RedisCommand.${cmd.methodName}(${params.decl}) = ${params.retMethod}(${params.call})")
            }
        }
    }

    class ParamResult(val decl: String, val call: String, val retMethod: String)

    fun RedisCommands.Command.toKotlinParams(): ParamResult {
        val ret = returnType.toKotlinMethodName()
        val nameParts = name.split(" ").map { it.quote() }
        val namePartsStr = nameParts.joinToString(", ")
        var errorAppend = ""
        try {
            if (params != null) {
                val decl = ArrayList<String>()
                val invoke = ArrayList<String>()
                invoke += nameParts
                for (param in params.params) {
                    if (param is RedisCommands.PARAM) {
                        decl += "${param.name}: ${param.kotlinType}"
                        invoke += param.name
                    } else {
                        TODO()
                    }
                }
                return ParamResult(
                    decl.joinToString(", "),
                    invoke.joinToString(", "),
                    ret
                )
            }
        } catch (e: NotImplementedError) {
            errorAppend = " /* TODO */"
        }
        return ParamResult(
            "vararg args: String$errorAppend",
            "$namePartsStr, *args",
            ret
        )
    }

    val RedisCommands.PARAM.kotlinType: String
        get() = when (this) {
            is RedisCommands.STR -> "String"
            is RedisCommands.INT -> "Int"
            else -> "Any?"
        }

    fun RedisCommands.RET.toKotlinMethodName(): String = when (this) {
        RedisCommands.RET_ANY -> "commandAny"
        RedisCommands.RET_ARRAY -> "commandArray"
        RedisCommands.RET_STRING -> "commandString"
        RedisCommands.RET_VOID -> "commandUnit"
        RedisCommands.RET_LONG -> "commandLong"
        else -> "commandAny"
    }
}
