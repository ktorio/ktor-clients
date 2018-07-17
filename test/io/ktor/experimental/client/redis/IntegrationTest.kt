package io.ktor.experimental.client.redis

import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.redis.protocol.*
import org.junit.*
import java.net.*
import kotlin.test.*

class IntegrationTest {

    @Test
    fun echoTest() = redisTest(address, REDIS_PASSWORD) {
        set("hello", "world")
        assertEquals("world", get("hello"))
    }

    @Test
    fun getSet(): Unit = redisTest(address, REDIS_PASSWORD) {
        val key = "key"

        for (value in listOf("", "a", "ab", "abc", "myalue", "a".repeat(1024), "a".repeat(2048))) {
            del(key)
            assertEquals(null, get(key))
            set(key, value)
            assertEquals(value, get(key))
            del(key)
        }
    }

    @Test
    fun authTest() {
        assertFailsWith<RedisException> {
            redisTest(address, password = "invalid") {
                set("hello", "world")
            }
        }
    }

    companion object {
        val REDIS_SERVICE = "redis"
        val REDIS_PORT = 6379
        val REDIS_PASSWORD = "myawesomepass"

        lateinit var address: InetSocketAddress

        @JvmField
        @ClassRule
        val docker = DockerComposeRule.builder()
            .file("resources/compose-redis.yml")
            .waitingForService("redis", HealthChecks.toHaveAllPortsOpen())
            .build()!!

        @BeforeClass
        @JvmStatic
        fun init() {
            val redis = docker
                .containers()
                .container(REDIS_SERVICE)
                .port(REDIS_PORT)!!

            address = InetSocketAddress(redis.ip, redis.externalPort)
        }
    }

}