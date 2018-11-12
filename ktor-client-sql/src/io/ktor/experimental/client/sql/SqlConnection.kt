package io.ktor.experimental.client.sql

import kotlinx.coroutines.*
import kotlinx.io.core.*

interface SqlConnection : CoroutineScope, Closeable {
    suspend fun query(queryString: String): QueryResult
}
