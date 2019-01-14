package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*
import kotlinx.io.core.*

class PostgreRow(
    override val result: SqlTable,
    private val data: List<ByteArray?>
) : SqlRow {

    override fun get(columnIndex: Int): SqlCell {
        return PostgreCell(this, columns[columnIndex], data[columnIndex])
    }

    override fun get(column: SqlColumn): SqlCell {
        val columnIndex = columns.indexOf(column)
        return get(columnIndex)
    }
}

class PostgreCell(
    override val row: SqlRow,
    override val column: SqlColumn,
    val content: ByteArray?
) : SqlCell {

    override fun asInt(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun asFloat(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun asDouble(): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun asString(): String = String(content!!)

    override fun asBytes(): ByteArray = content!!

    override fun asByte(): Byte {
        check(content!!.size == 1) { "" }
        return content!![0]
    }

    override fun asBoolean(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun asPacket(): ByteReadPacket = buildPacket {
        writeFully(content!!)
    }

    override fun <T> asArray(): Array<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> asType(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNull(): Boolean = content == null
}
