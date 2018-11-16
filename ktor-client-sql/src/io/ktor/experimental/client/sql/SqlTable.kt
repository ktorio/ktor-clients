package io.ktor.experimental.client.sql

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

interface SqlTable : ReceiveChannel<SqlRow>,
    CoroutineScope {
    val columns: List<SqlColumn>
}