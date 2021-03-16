package io.ktor.experimental.client.postgre

import kotlinx.coroutines.*
import java.net.*

fun postgreTest(
    address: InetSocketAddress,
    database: String = "postgres", user: String = "myuser", password: String = "hello",
    block: suspend PostgreClient.() -> Unit
): Unit = runBlocking {
    PostgreClient(address, database, user, password).use { it.block() }
}
