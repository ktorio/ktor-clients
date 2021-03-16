package io.ktor.experimental.client.sql

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*

interface SqlConnection : CoroutineScope, Closeable {
    suspend fun query(queryString: String): QueryResult
}
