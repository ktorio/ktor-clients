package io.ktor.experimental.client.sql

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*

interface ResultCell : CoroutineScope {
    val row: ResultRow

    val column: SqlColumn

    fun asInt(): Int

    fun asFloat(): Float

    fun asDouble(): Double

    fun asString(): String

    fun asBytes(): ByteArray

    fun asByte(): Byte

    fun asBoolean(): Boolean

    fun asPacket(): ByteReadPacket

    fun isNull(): Boolean

    fun <T> asArray(): Array<T>

    fun <T> asType(): T
}
