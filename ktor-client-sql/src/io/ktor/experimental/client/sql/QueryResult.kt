package io.ktor.experimental.client.sql

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

typealias QueryResult = Channel<QueryResultTable>

interface QueryResultTable : ReceiveChannel<ResultRow>, CoroutineScope {

    val columns: List<SqlColumn>
}

