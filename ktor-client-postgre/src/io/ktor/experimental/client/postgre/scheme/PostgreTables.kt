package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*
import kotlinx.coroutines.channels.*

class PostgreTables(
    private val channel: ReceiveChannel<SqlTable>
) : SqlTables(), ReceiveChannel<SqlTable> by channel
