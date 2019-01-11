package io.ktor.experimental.client.sql

import kotlinx.coroutines.channels.*

interface SqlQueryResult

object SqlEmptyResult : SqlQueryResult

inline class SqlBatchResult<T : SqlQueryResult>(val results: Channel<T>): SqlQueryResult

inline class SqlMessage(val message: String) : SqlQueryResult

abstract class SqlTables : SqlQueryResult, ReceiveChannel<SqlTable>

abstract class SqlStatement : SqlQueryResult {
    abstract fun execute(vararg parameters: String): SqlQueryResult
}
