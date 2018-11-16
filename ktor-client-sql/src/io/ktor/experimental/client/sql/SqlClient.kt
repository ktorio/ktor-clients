package io.ktor.experimental.client.sql

import kotlinx.coroutines.*
import kotlinx.io.core.*
import java.lang.IllegalStateException
import kotlin.reflect.*

interface SqlClient : CoroutineScope, Closeable {

    suspend fun execute(queryString: String): SqlQueryResult

    suspend fun prepare(queryString: String): SqlStatement

    suspend fun connection(): SqlConnection
}

suspend fun <R> SqlClient.connection(block: suspend (connection: SqlConnection) -> R): R =
    connection().use { block(it) }

suspend inline fun SqlClient.select(queryString: String): SqlTables {
    val result = execute(queryString)
    if (result !is SqlTables) throw SqlQueryTypeMismatchException(SqlTables::class, result)
    return result
}

class SqlQueryTypeMismatchException(
    expectedType: KClass<*>, result: SqlQueryResult
) : IllegalStateException("Fail to execute query. Expected type: $expectedType, but got $result")
