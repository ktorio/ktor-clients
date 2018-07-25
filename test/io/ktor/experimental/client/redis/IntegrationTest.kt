package io.ktor.experimental.client.redis

import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.redis.protocol.*
import kotlinx.coroutines.experimental.*
import org.junit.*
import org.junit.Test
import java.net.*
import kotlin.test.*

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
    fun testSets(): Unit = redisTest {
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
        cleanSetKeys(key1, key2) {
            sadd(key1, "a", "b")
            sadd(key2, "c")
            assertEquals(true, smove(key1, key2, "b"))
            assertEquals(false, smove(key1, key2, "c"))
            assertEquals(setOf("a"), smembers(key1))
            assertEquals(setOf("b", "c"), smembers(key2))
        }

        // SPOP (single)
        cleanSetKeys(key1) {
            sadd(key1, "a")
            assertEquals("a", spop(key1))
            assertEquals(null, spop(key1))
        }

        // SPOP (multi)
        cleanSetKeys(key1) {
            sadd(key1, "a", "b", "c")
            assertEquals(setOf("a", "b", "c"), spop(key1, 3))
            assertEquals(setOf(), spop(key1, 3))
        }

        // SRANDMEMBER
        cleanSetKeys(key1) {
            sadd(key1, "a", "b", "c")
            assertTrue(srandmember(key1) in setOf("a", "b", "c"))
            assertEquals(setOf("a", "b", "c"), srandmember(key1, 3))
        }

        // SSCAN
        // @TODO
    }

    @Test
    fun testMaps(): Unit = redisTest {
        val key = "myhashmap1"
        val key2 = "myhashmap2"

        // HSET, HGET, HDEL, HEXISTS, HKEYS, HVALS, HGETALL
        run {
            del(key)
            hset(key, "hello", "world")
            assertEquals("world", hget(key, "hello"))
            assertEquals(true, hexists(key, "hello"))
            assertEquals(1L, hlen(key))
            assertEquals(setOf("hello"), hkeys(key))
            assertEquals(setOf("world"), hvals(key))
            assertEquals(mapOf("hello" to "world"), hgetall(key))
            hdel(key, "hello")
            assertEquals(null, hget(key, "hello"))
            assertEquals(false, hexists(key, "hello"))
            assertEquals(0L, hlen(key))
            assertEquals(setOf(), hkeys(key))
            assertEquals(setOf(), hvals(key))
            assertEquals(mapOf(), hgetall(key))
        }

        // HSETNX
        run {
            del(key)
            val field = "hello2"
            val value1 = "test"
            val value2 = "test2"

            assertEquals(true, hsetnx(key, field, value1)) // Set because key not existed yet
            assertEquals(value1, hget(key, field))

            assertEquals(false, hsetnx(key, field, value2)) // Not re-set because key existed already
            assertEquals(value1, hget(key, field))
        }

        // HSTRLEN
        run {
            del(key)
            hset(key, "hello", "worldworld")
            assertEquals("worldworld".length.toLong(), hstrlen(key, "hello"))
            assertEquals(0L, hstrlen(key, "non-existant-key"))
        }

        // HINCR, HINCRFLOAT
        run {
            del(key)
            assertEquals(10L, hincrby(key, "int", 10L))
            assertEquals(20L, hincrby(key, "int", 10L))
            hset(key, "int", "100")
            assertEquals(100L, hincrby(key, "int", 0L))

            assertEquals(10.5, hincrbyfloat(key, "double", 10.5))
            assertEquals(21.0, hincrbyfloat(key, "double", 10.5))
            hset(key, "double", "100.5")
            assertEquals(100.5, hincrbyfloat(key, "double", 0.0))
        }

        // HMSET, HMGET
        run {
            hmset(key, "akey" to "avalue", "bkey" to "bvalue", "ckey" to "cvalue")
            hmset(key2, mapOf("akey" to "avalue", "bkey" to "bvalue", "ckey" to "cvalue"))
            assertEquals(listOf("avalue", "cvalue", "bvalue"), hmget(key, "akey", "ckey", "bkey"))
            assertEquals(listOf("avalue", "cvalue", "bvalue"), hmget(key2, "akey", "ckey", "bkey"))
        }

        // HSCAN
        // @TODO
    }

    @Test
    fun testSortedSets() = redisTest {
        val key = "mysortedset"

        // ZADD (variants), ZCARD, ZCOUNT, ZSCORE
        run {
            del(key)
            assertEquals(0, zcard(key))
            assertEquals(0, zcount(key))
            zadd(key, "hello" to 10.0)
            zadd(key, mapOf("world" to 20.0, "demo" to 0.0))
            zadd(key, "hi" to 30.0, "there" to 40.0)
            assertEquals(5, zcard(key))
            assertEquals(5, zcount(key))
            assertEquals(3, zcount(key, 10.0, 30.0))
            assertEquals(2, zcount(key, 10.0, 30.0, includeMin = false))
            assertEquals(1, zcount(key, 10.0, 30.0, includeMin = false, includeMax = false))
            assertEquals(10.0, zscore(key, "hello"))
            zincrby(key, "hello", 2.5)
            assertEquals(12.5, zscore(key, "hello"))
        }
        // ZLEXCOUNT ( https://redis.io/commands/zlexcount )
        run {
            del(key)
            zadd(key, "a" to 0.0, "b" to 0.0, "c" to 0.0, "d" to 0.0, "e" to 0.0)
            zadd(key, "f" to 0.0, "g" to 0.0)
            assertEquals(7, zlexcount(key, "-", "+"))
            assertEquals(5, zlexcount(key, "[b", "[f"))
        }
        // ZPOPMAX, ZPOPMIN
        // Disabled until Redis 5.0.0 is released
        //run {
        //    del(key)
        //    zadd(key, "one" to 1.0, "two" to 2.0, "three" to 3.0)
        //    assertEquals(mapOf("three" to 3.0), zpopmax(key))
        //    assertEquals(mapOf("one" to 1.0), zpopmin(key))
        //}
    }

    @Test
    fun testScripting() = redisTest {
        run {
            assertEquals(
                listOf("key1", "key2", "first", "second"),
                eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", "key1" to "first", "key2" to "second")
            )
            assertEquals(
                listOf("key1", "key2", "first", "second"),
                eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", mapOf("key1" to "first", "key2" to "second"))
            )
        }
        run {
            assertEquals(
                "a42059b356c875f0717db19a51f6aaca9ae659ea",
                scriptLoad("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}")
            )
            assertEquals(
                listOf("key1", "key2", "first", "second"),
                evalsha("a42059b356c875f0717db19a51f6aaca9ae659ea", "key1" to "first", "key2" to "second")
            )
            assertEquals(
                listOf("key1", "key2", "first", "second"),
                evalsha("a42059b356c875f0717db19a51f6aaca9ae659ea", mapOf("key1" to "first", "key2" to "second"))
            )
            assertEquals(
                listOf(true, false),
                scriptExists("a42059b356c875f0717db19a51f6aaca9ae659ea", "0000000000000000000000000000000000000000")
            )
            scriptFlush()
            assertEquals(
                listOf(false, false),
                scriptExists("a42059b356c875f0717db19a51f6aaca9ae659ea", "0000000000000000000000000000000000000000")
            )
        }
        run {
            // https://redis.io/commands/script-debug
            scriptDebug(RedisScriptDebug.No)
            // @TODO: Check that the LDB port is open with Yes and Sync
        }
        run {
            // @TODO: Doesn't work.
            launch(start = CoroutineStart.UNDISPATCHED) {
                try {
                    eval(
                        """
                            while (1) do
                            end
                        """.trimIndent()
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            delay(300)
            scriptKill()
        }
    }

    private suspend inline fun Redis.cleanSetKeys(vararg keys: String, callback: () -> Unit) {
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

    private fun redisTest(
        password: String = REDIS_PASSWORD,
        cleanup: suspend Redis.() -> Unit = {},
        callback: suspend Redis.() -> Unit
    ) =
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