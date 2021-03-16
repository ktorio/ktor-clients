package io.ktor.experimental.client.postgre

import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlin.test.*

class PostgreTest {

    @Test
    fun testWritePostgreStartup() = runBlocking {
        for (useByteReadReadInt in listOf(true, false)) {
            val response = ByteChannel(true).apply { writePostgreStartup("hello") }
                .readPostgrePacket(startUp = true)
                .payload
                .readText()

            val expected =
                "\u0000\u0003\u0000\u0000user\u0000hello\u0000database\u0000hello\u0000application_name\u0000ktor-cio\u0000client_encoding\u0000UTF8\u0000\u0000"
            assertEquals(expected, response)
        }
    }
}
