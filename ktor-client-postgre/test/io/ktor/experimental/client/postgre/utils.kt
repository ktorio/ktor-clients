package io.ktor.experimental.client.postgre

import kotlinx.coroutines.*
import kotlinx.coroutines.debug.*
import kotlinx.io.core.*
import java.net.*
import kotlin.system.*

fun postgreTest(
    address: InetSocketAddress,
    database: String = "postgres", user: String = "myuser", password: String = "hello",
    block: suspend PostgreClient.() -> Unit
): Unit = runBlocking {
    DebugProbes.install()
    PostgreClient(address, database, user, password).use { it.block() }

    DebugProbes.dumpCoroutines()
}
