package io.ktor.experimental.client.redis

import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.redis.protocol.*
import java.net.*
import kotlin.test.*
import org.junit.*
import kotlin.test.Test

class IntegrationTest {

    @Test
    fun echoTest() = redisTest {
        set("hello", "world")
        assertEquals("world", get("hello"))
    }

    @Test
    fun getSet(): Unit = redisTest {
        val key = "key"

        for (value in listOf("", "a", "ab", "abc", "myalue", "a".repeat(1024), "a".repeat(2048))) {
            del(key)
            assertEquals(null, get(key))
            set(key, value)
            assertEquals(value, get(key))
            del(key)
        }
    }

    @Test(expected = RedisException::class)
    fun invalidAuth() = redisTest(password = "invalid") {
        set("hello", "world")
    }

    @Test
    fun sets(): Unit = redisTest {
        val key = "myset"
        val key1 = "myset1"
        val key2 = "myset2"
        val key3 = "myset3"
        val key4 = "myset4"
        del(key, key1, key2, key3, key4)

        // EMPTY: SCARD + SMEMBERS
        assertEquals(0, scard(key))
        assertEquals(setOf(), smembers(key))

        // TWO-ELEMENTS: SADD
        sadd(key, "hello")
        sadd(key, "world")
        assertEquals(2, scard(key))
        assertEquals(setOf("hello", "world"), smembers(key))

        // ONE-ELEMENT: SREM + SISMEMBER
        srem(key, "hello")
        assertEquals(setOf("world"), smembers(key))
        assertEquals(false, sismember(key, "hello"))
        assertEquals(true, sismember(key, "world"))

        // MULTI SADD/SREM
        sadd(key, "a", "b", "c", "d")
        srem(key, "b", "c")
        assertEquals(setOf("world", "a", "d"), smembers(key))

        // Preparing sets for BOOLEAN ops
        sadd(key1, "a", "b", "c", "d", "e")
        sadd(key2, "b", "c", "f")
        sadd(key3, "c", "d", "g")

        // SDIFF & SDIFFSTORE (-)
        assertEquals(setOf("a", "e"), sdiff(key1, key2, key3))
        assertEquals(2, sdiffstore(key4, key1, key2, key3))
        assertEquals(setOf("a", "e"), smembers(key4))

        // SINTER & SINTERSTORE (^)
        assertEquals(setOf("c"), sinter(key1, key2, key3))
        assertEquals(1, sinterstore(key4, key1, key2, key3))
        assertEquals(setOf("c"), smembers(key4))

        // SUNION & SUNIONSTORE (+)
        assertEquals(setOf("a", "b", "c", "d", "e", "f", "g"), sunion(key1, key2, key3))
        assertEquals(7, sunionstore(key4, key1, key2, key3))
        assertEquals(setOf("a", "b", "c", "d", "e", "f", "g"), smembers(key4))

        // SMOVE
        cleanKeys(key1, key2) {
            sadd(key1, "a", "b")
            sadd(key2, "c")
            assertEquals(true, smove(key1, key2, "b"))
            assertEquals(false, smove(key1, key2, "c"))
            assertEquals(setOf("a"), smembers(key1))
            assertEquals(setOf("b", "c"), smembers(key2))
        }

        // SPOP (single)
        cleanKeys(key1) {
            sadd(key1, "a")
            assertEquals("a", spop(key1))
            assertEquals(null, spop(key1))
        }

        // SPOP (multi)
        cleanKeys(key1) {
            sadd(key1, "a", "b", "c")
            assertEquals(setOf("a", "b", "c"), spop(key1, 3))
            assertEquals(setOf(), spop(key1, 3))
        }

        // SRANDMEMBER
        cleanKeys(key1) {
            sadd(key1, "a", "b", "c")
            assertTrue(srandmember(key1) in setOf("a", "b", "c"))
            assertEquals(setOf("a", "b", "c"), srandmember(key1, 3))
        }

        // SSCAN
        // @TODO
    }

    private suspend inline fun Redis.cleanKeys(vararg keys: String, callback: () -> Unit) {
        val keysMembers = keys.map { it to if (exists(it)) smembers(it) else null }
        del(*keys)
        try {
            callback()
        } finally {
            del(*keys)
            for ((key, members) in keysMembers) {
                if (members != null) {
                    sadd(key, *members.toTypedArray())
                }
            }
        }
    }

    private fun redisTest(password: String = REDIS_PASSWORD, cleanup: suspend Redis.() -> Unit = {}, callback: suspend Redis.() -> Unit) =
        redisTest(address, password) {
            cleanup()
            try {
                callback()
            } finally {
                cleanup()
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