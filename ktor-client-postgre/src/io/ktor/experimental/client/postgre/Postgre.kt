package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.scheme.*
import io.ktor.experimental.client.postgre.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.io.core.*
import java.net.*

// https://www.postgresql.org/docs/11/static/index.html

class PostgreClient(
    val address: InetSocketAddress,
    val database: String = "default",
    val user: String = "root",
    private val password: String? = null,
    val maxConnections: Int = 32
) : Closeable {
    val context: Job = Job()
    private val requests = Channel<PipelineElement<String, PostgreRawResponse>>()

    fun newConnection() {
        val connection: PostgreConnection = PostgreConnection(
            address, user, password, database, requests
        )

        TODO()
    }
    suspend fun query(queryString: String): PostgreRawResponse {
        val result = CompletableDeferred<PostgreRawResponse>()
        requests.send(PipelineElement(queryString, result))
        return result.await()
    }

    suspend fun withConnection(block: PostgreClient.() -> Unit): Unit = TODO()

    override fun close() {
        requests.close()
    }
}
