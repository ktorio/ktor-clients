package io.ktor.experimental.client.sql

import kotlinx.coroutines.*

interface SqlRow : CoroutineScope {
    val result: SqlTable

    val columns: List<SqlColumn> get() = result.columns

    operator fun get(column: SqlColumn): SqlCell
    operator fun get(columnIndex: Int): SqlCell
}

operator fun SqlRow.get(columnName: String): SqlCell = columns.find {
    it.name.equals(columnName, ignoreCase = true)
}?.let { this[it] }
    ?: throw ColumnNotFoundException(columnName, this)

class ColumnNotFoundException(
    val columnName: String,
    val row: SqlRow
) : IllegalArgumentException("Column not found: $columnName in $row")
