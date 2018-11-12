package io.ktor.experimental.client.sql

import kotlinx.coroutines.*

interface ResultRow : CoroutineScope {

    val result: QueryResultTable

    val columns: List<SqlColumn> get() = result.columns

    operator fun get(column: SqlColumn): ResultCell

}

operator fun ResultRow.get(columnIndex: Int): ResultCell = get(columns[columnIndex])

operator fun ResultRow.get(columnName: String): ResultCell = columns.find {
    it.name.equals(columnName, ignoreCase = true)
}?.let { this[it] }
    ?: throw ColumnNotFoundException(columnName, this)

class ColumnNotFoundException(val columnName: String, val queryRow: ResultRow) : IllegalArgumentException("Column not found: $columnName in $queryRow")
