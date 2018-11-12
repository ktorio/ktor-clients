package io.ktor.experimental.client.postgre

import kotlinx.coroutines.*
import kotlinx.io.core.*
import java.net.*
import kotlin.system.*

fun postgreTest(
    address: InetSocketAddress,
    database: String = "postgres", user: String = "myuser", password: String = "hello",
    block: suspend PostgreClient.() -> Unit
): Unit = runBlocking {
    PostgreClient(address, database, user, password).use { it.block() }
}
