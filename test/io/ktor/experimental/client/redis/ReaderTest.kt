package io.ktor.experimental.client.redis

import io.ktor.experimental.client.redis.protocol.*
import kotlinx.coroutines.experimental.*
import org.junit.Test
import kotlin.test.*


class ReaderTest {

    @Test
    fun errorTest() {
        val error = buildChannel {
            writeStringUtf8("-ERROR\r\n")
        }

        assertFailsWith<RedisException>("ERROR") {
            runBlocking {
                error.readRedisMessage()
            }
        }
    }
}