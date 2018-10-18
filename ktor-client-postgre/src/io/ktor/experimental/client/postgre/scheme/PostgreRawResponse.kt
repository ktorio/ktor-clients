package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.postgre.protocol.*
import kotlinx.io.charsets.*

class PostgreRawResponse(
    val info: String,
    val notice: PostgreException?,
    val columns: List<PostgreColumn>,
    val rawRows: List<List<ByteArray?>>
) {
    val rows: List<PostgreRowView> = rawRows.map { PostgreRowView(columns, it) }

    override fun toString(): String = info
}

class PostgreRowView(
    private val column: List<PostgreColumn>,
    private val rawRow: List<ByteArray?>
) : Iterable<PostgreCellView?> {

    fun getByte(index: Int): Byte = cell(index)!!.byte()

    fun getBytes(index: Int): ByteArray = cell(index)!!.bytes()

    fun getInt(index: Int): Int = cell(index)!!.int()

    fun getLong(index: Int): Long = cell(index)!!.long()

    fun getString(index: Int, charset: Charset = Charsets.UTF_8): String = cell(index)!!.string(charset)

    fun isNull(index: Int): Boolean = rawRow[index] == null

    fun cell(index: Int): PostgreCellView? = rawRow[index]?.let { PostgreCellView(it) }

    fun size(): Int = rawRow.size

    override fun iterator(): Iterator<PostgreCellView?> = object : Iterator<PostgreCellView?> {
        private var index = 0

        override fun hasNext(): Boolean = index < rawRow.size

        override fun next(): PostgreCellView? {
            index++
            return cell(index)
        }
    }
}

class PostgreCellView(
    private val rawCell: ByteArray
) {
    fun byte(): Byte {
        check(rawCell.size == 1)
        return rawCell.first()
    }

    fun bytes(): ByteArray = rawCell

    fun int(): Int = TODO()

    fun long(): Long = TODO()

    fun string(charset: Charset = Charsets.UTF_8) = rawCell.toString(charset)
}
