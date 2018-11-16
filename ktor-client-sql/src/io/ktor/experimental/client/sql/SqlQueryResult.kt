package io.ktor.experimental.client.sql

import kotlinx.coroutines.channels.*

sealed class SqlQueryResult

object SqlEmptyResult : SqlQueryResult()

class SqlMessage(val message: String) : SqlQueryResult()

abstract class SqlTables : SqlQueryResult(), ReceiveChannel<SqlTable>

abstract class SqlStatement : SqlQueryResult() {
    abstract fun execute(vararg parameters: String): SqlQueryResult
}
