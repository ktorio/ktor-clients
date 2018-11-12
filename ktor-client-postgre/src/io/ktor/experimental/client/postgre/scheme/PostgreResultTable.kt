package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*


class PostgreResultTable(
    override val columns: List<SqlColumn>,
    internal val rows: Channel<PostgreRow>,
    override val coroutineContext: CoroutineContext
) : QueryResultTable, ReceiveChannel<ResultRow> by rows {

    override fun toString(): String = "PostgreResultTable(${columns.joinToString()})"
}