package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.connection.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.io.core.*
import java.net.*
import kotlin.coroutines.*

internal typealias SqlRequest = PipelineElement<String, SqlQueryResult>

// https://www.postgresql.org/docs/11/static/index.html
class PostgreClient(
    val address: InetSocketAddress,
    val database: String = "default",
    val user: String = "root",
    private val password: String? = null,
    val maxConnections: Int = 1
) : SqlClient, Closeable {
    override val coroutineContext: CoroutineContext =
        Dispatchers.Default + SupervisorJob() + CoroutineName("ktor-postgre-client")

    private val requests = Channel<SqlRequest>()

    init {
        List(maxConnections) {
            coroutineContext.PostgreConnectionPipeline(
                address, database, user, password, requests
            )
        }
    }

    override suspend fun connection(): SqlConnection = PostgreConnection(
        address, database, user, password, coroutineContext
    )

    override suspend fun execute(queryString: String): SqlQueryResult = deferred {
        requests.send(SqlRequest(queryString, it))
    }

    override suspend fun prepare(queryString: String): SqlStatement {
        requests.send(SqlRequest)
    }

    override fun close() {
        requests.close()
    }
}
