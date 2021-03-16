package io.ktor.experimental.client.postgre

import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*


internal fun String.postgreEscape(): String {
    var out = ""
    for (c in this) {
        when (c) {
            '\u0000' -> out += "\\0"
            '\'' -> out += "\\'"
            '\"' -> out += "\\\""
            '\b' -> out += "\\b"
            '\n' -> out += "\\n"
            '\r' -> out += "\\r"
            '\t' -> out += "\\t"
            '\u0026' -> out += "\\Z"
            '\\' -> out += "\\\\"
            '%' -> out += "\\%"
            '_' -> out += "\\_"
            '`' -> out += "\\`"
            else -> out += c
        }
    }
    return out
}

internal fun Output.writeCString(value: String, charset: Charset = Charsets.UTF_8) {
    val data = value.toByteArray(charset)
    writeFully(data)
    writeByte(0)
}

internal fun Input.readCString(charset: Charset = Charsets.UTF_8): String = buildPacket {
    readUntilDelimiter(0, this)

    // skip delimiter
    readByte()
}.readText(charset)

internal fun String.postgreQuote(): String = "'${this.postgreEscape()}'"

internal fun String.postgreTableQuote(): String = "`${this.postgreEscape()}`"
