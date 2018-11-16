package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*


class PostgreTable(
    override val columns: List<SqlColumn>,
    internal val rows: Channel<PostgreRow>,
    override val coroutineContext: CoroutineContext
) : SqlTable, ReceiveChannel<SqlRow> by rows {

    override fun toString(): String = "PostgreResultTable(${columns.joinToString()})"
}