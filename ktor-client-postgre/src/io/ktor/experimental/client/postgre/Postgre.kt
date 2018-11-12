package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.connection.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.io.core.*
import org.slf4j.*
import java.net.*
import kotlin.coroutines.*

internal typealias SqlRequest = PipelineElement<String, QueryResult>

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
            PostgreConnection(
                address, database,
                user, password,
                requests, coroutineContext
            )
        }
    }

    override suspend fun connection(): SqlConnection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun query(queryString: String): QueryResult = deferred {
        requests.send(SqlRequest(queryString, it))
    }

    override suspend fun prepare(): PreparedQuery {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun query(query: PreparedQuery): QueryResultTable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        requests.close()
    }
}
