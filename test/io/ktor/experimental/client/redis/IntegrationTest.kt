package io.ktor.experimental.client.redis

import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.redis.protocol.*
import kotlinx.coroutines.experimental.*
import org.junit.*
import org.junit.Ignore
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
    }

    @Test
    @Ignore("ScriptKill doesn't seems to work yet. Disable for now.")
    fun testScriptingKill() = redisTest {
        run {
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

    private val key1 = "key1"

    @Test
    fun testList() = redisTest(cleanup = {
        del(key1)
    }) {
        suspend fun lgetallStr(key: String) = lgetall(key1).joinToString("")

        // lpush, rpush, llen, lrange
        run {
            del(key1)
            assertEquals(0, llen(key1))
            assertEquals(listOf(), lgetall(key1))
            rpush(key1, "hello")
            rpush(key1, "world")
            assertEquals(2, llen(key1))
            assertEquals(listOf("hello", "world"), lgetall(key1))
            rpush(key1, "hi", "there")
            assertEquals(listOf("hello", "world", "hi", "there"), lgetall(key1))
            lpush(key1, "nice")
            lpush(key1, "works", "it")
            assertEquals(listOf("it", "works", "nice", "hello", "world", "hi", "there"), lgetall(key1))
            assertEquals(listOf("it", "works", "nice"), lrange(key1, 0L until 3L))
            assertEquals(listOf("hi", "there"), lrange(key1, -2L until 0L))
        }
        // lrem, lset
        run {
            del(key1)
            rpush(key1, ".", ".", ".", "*", "*", ".", ".", ".")
            lrem(key1, 2, ".")
            assertEquals(".**...", lgetallStr(key1))
            lrem(key1, -2, ".")
            assertEquals(".**.", lgetallStr(key1))
            lset(key1, 1L, "-")
            assertEquals(".-*.", lgetallStr(key1))
            lset(key1, -2L, "+")
            assertEquals(".-+.", lgetallStr(key1))
        }
        // ltrim
        run {
            del(key1)
            rpush(key1, "0", "1", "2", "3", "4", "5", "6", "7")
            ltrim(key1, 1L..5L)
            assertEquals("12345", lgetallStr(key1))
        }
        // lpop, rpop
        run {
            del(key1)
            rpush(key1, "a", "b", "c")
            rpop(key1)
            assertEquals("ab", lgetallStr(key1))
            lpop(key1)
            assertEquals("b", lgetallStr(key1))
        }
        // linsertBefore, linsertAfter
        run {
            del(key1)
            rpush(key1, "-", "b", "-")
            linsertBefore(key1, "b", "a")
            linsertAfter(key1, "b", "c")
            assertEquals("-abc-", lgetallStr(key1))
        }
        // lindex
        run {
            del(key1)
            rpush(key1, "0", "1", "2")
            assertEquals("0", lindex(key1, 0))
            assertEquals("1", lindex(key1, 1))
            assertEquals("2", lindex(key1, 2))
            assertEquals("2", lindex(key1, -1))
        }
    }

    @Test
    fun testKeys() = redisTest {
        val (key1, value1) = "key1" to "value1"
        val (key2, value2) = "key2" to "value2"
        val (key3, value3) = "key3" to "value3"
        val (key4, _) = "key4" to "value4"

        // del
        run {
            set(key1, value1)
            set(key2, value2)
            set(key3, value3)
            del(key1, key2, key4)
            assertEquals(null, get(key1))
            assertEquals(null, get(key2))
            assertEquals(value3, get(key3))
            assertEquals(null, get(key4))
            del(key3)
            assertEquals(null, get(key3))
        }

        // exists
        run {
            set(key1, value1)
            set(key2, value2)
            set(key3, value3)
            del(key1, key2, key4)
            assertEquals(null, get(key1))
            assertEquals(null, get(key2))
            assertEquals(value3, get(key3))
            assertEquals(null, get(key4))
            del(key3)
            assertEquals(null, get(key3))
        }

        // @TODO: Not working yet: "ERR DUMP payload version or checksum are wrong"
        // dump/restore
        //run {
        //    val mykey = "mykey"
        //    val myvalue = "10"
        //    set(mykey, myvalue)
        //    val mykeyDump = dump(mykey)
        //    println(mykeyDump!!.size)
        //    expectException<RedisException>("BUSYKEY Target key name already exists.") { restore(mykey, mykeyDump) }
        //    del(mykey)
        //    assertEquals("OK", restore(mykey, mykeyDump))
        //    assertEquals(value1, get(mykey))
        //}

        // object
        run {
            del("mylist")
            lpush("mylist", "Hello World")
            assertEquals(1L, objectRefcount("mylist"))
            assertTrue(objectEncoding("mylist")!!.contains("list"))
            assertEquals(0L, objectIdletime("mylist"))
            // assertEquals(0L, objectFreq("mylist")) // Requires LFU: ERR An LFU maxmemory policy is not selected, access frequency not tracked. Please note that when switching between policies at runtime LRU and LFU data will take some time to adjust.
            assertTrue(objectHelp()!!.isNotBlank())
        }
    }

    @Test
    fun testServer() = redisTest {
        //run {
        //    println(clientList())
        //}
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

    inline fun <reified T : Any> expectException(message: String? = null, callback: () -> Unit) {
        try {
            callback()
        } catch (e: Throwable) {
            if (T::class.isInstance(e)) {
                if (message != null) {
                    assertEquals(message, e.message)
                }
            } else {
                throw e
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