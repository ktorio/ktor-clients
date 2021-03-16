package io.ktor.experimental.client.sql

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*

interface PreparedQuery

interface SqlClient : CoroutineScope, Closeable {

    suspend fun query(queryString: String): QueryResult

    suspend fun prepare(): PreparedQuery

    suspend fun query(query: PreparedQuery): QueryResultTable

    suspend fun connection(): SqlConnection
}

suspend fun <R> SqlClient.connection(block: suspend (connection: SqlConnection) -> R): R =
    connection().use { block(it) }