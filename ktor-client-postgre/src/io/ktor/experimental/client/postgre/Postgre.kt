package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.connection.*
import io.ktor.experimental.client.sql.*
import io.ktor.experimental.client.util.*
import io.ktor.network.selector.*
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

    private val selectorManager = ActorSelectorManager(coroutineContext)

    private val requests = Channel<PipelineElement<String, SqlQueryResult>>()

    init {
        List(maxConnections) {
            coroutineContext.PostgreConnectionPipeline(
                selectorManager, address, database, user, password, requests
            )
        }
    }

    override suspend fun connection(): SqlConnection = PostgreConnection(
        selectorManager, address, database, user, password, coroutineContext
    )

    override suspend fun execute(queryString: String): SqlQueryResult = deferred {
        requests.send(PipelineElement(queryString, it))
    }

    override suspend fun prepare(queryString: String): SqlStatement = deferred<SqlQueryResult> {
        requests.send(PipelineElement(queryString, it))
    } as SqlStatement

    override fun close() {
        requests.close()
        coroutineContext.cancel()
    }
}
