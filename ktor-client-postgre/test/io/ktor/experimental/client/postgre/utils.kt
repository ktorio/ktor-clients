package io.ktor.experimental.client.postgre

import kotlinx.coroutines.*
import kotlinx.coroutines.debug.*
import kotlinx.io.core.*
import java.net.*

internal fun postgreTest(
    address: InetSocketAddress,
    database: String = "postgres", user: String = "myuser", password: String = "hello",
    block: suspend PostgreClient.() -> Unit
): Unit {
    DebugProbes.install()

    runBlocking {
        val client = PostgreClient(address, database, user, password)
        client.use { it.block() }
        DebugProbes.dumpCoroutines()
        client.coroutineContext[Job]!!.join()
    }.also {
        DebugProbes.dumpCoroutines()
    }
}
