package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.util.*
import kotlinx.coroutines.channels.*
import java.net.*
import kotlin.coroutines.*

class PostgreConnection internal constructor(
    address: InetSocketAddress, database: String,
    username: String, password: String?,
    override val coroutineContext: CoroutineContext
) : SqlConnection {
    val requestChannel: Channel<SqlRequest> = Channel()

    private val pipeline = coroutineContext.PostgreConnectionPipeline(
        address, database, username, password, requestChannel
    )

    override suspend fun execute(queryString: String): SqlQueryResult = deferred {
        requestChannel.send(SqlRequest(queryString, it))
    }

    override suspend fun prepare(queryString: String): SqlStatement = deferred {
    }

    override fun close() {
        pipeline.close()
    }
}
