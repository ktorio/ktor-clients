package io.ktor.experimental.client.redis

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.*
import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.redis.protocol.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import org.junit.*
import org.junit.Ignore
import org.junit.Test
import java.net.*
import java.util.*
import kotlin.math.*
import kotlin.system.*
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
            hset(key, "hi" to "there", "hello" to "world")
            assertEquals(2L, hlen(key))
            hdel(key, "hi", "hello")
            assertEquals(0L, hlen(key))
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
    private val key2 = "key2"
    private val key3 = "key3"
    private val key4 = "key4"

    @Test
    fun testList() = redisTest(cleanup = {
        del(key1)
        del(key2)
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
        // lpushx, rpushx
        run {
            del(key1)
            del(key2)
            rpush(key1, "hello")
            rpushx(key1, "world")
            rpushx(key2, "world")
            lpushx(key1, "hey")
            lpushx(key2, "hey")
            assertEquals("hey hello world", lgetall(key1).joinToString(" "))
            assertEquals("", lgetall(key2).joinToString(" "))
        }
        // rpoplpush
        run {
            del(key1)
            del(key2)
            rpush(key1, "a", "b")
            assertEquals("b", rpoplpush(key1, key2))
            assertEquals("a", rpoplpush(key1, key2))
            assertEquals(null, rpoplpush(key1, key2))
            assertEquals("", lgetall(key1).joinToString(" "))
            assertEquals("a b", lgetall(key2).joinToString(" "))
        }
    }

    @Test
    @Ignore("Slow tests because timeouts are in econds")
    fun testListBlocking() = redisTest {
        // brpop
        run {
            del(key1)
            del(key2)
            rpush(key1, "a", "b")
            rpush(key2, "c")
            assertEquals(key1 to "b", brpop(key1, key2, timeout = 1))
            assertEquals(key1 to "a", brpop(key1, key2, timeout = 1))
            assertEquals(key2 to "c", brpop(key1, key2, timeout = 1))
            val time = measureTimeMillis {
                assertEquals(null, brpop(key1, key2, timeout = 1))
            }
            assertTrue { time >= 1000 }
        }
        // blpop
        run {
            del(key1)
            del(key2)
            rpush(key1, "a", "b")
            rpush(key2, "c")
            assertEquals(key1 to "a", blpop(key1, key2, timeout = 1))
            assertEquals(key1 to "b", blpop(key1, key2, timeout = 1))
            assertEquals(key2 to "c", blpop(key1, key2, timeout = 1))
            val time = measureTimeMillis {
                assertEquals(null, blpop(key1, key2, timeout = 1))
            }
            assertTrue { time >= 1000 }
        }
        // brpoplpush
        run {
            del(key1)
            del(key2)
            rpush(key1, "a", "b")
            assertEquals("b", brpoplpush(key1, key2, timeout = 1))
            assertEquals("a", brpoplpush(key1, key2, timeout = 1))
            val time = measureTimeMillis {
                assertEquals(null, brpoplpush(key1, key2, timeout = 1))
            }
            assertTrue { time >= 1000 }
        }
    }

    @Test
    fun testKeys() = redisTest {
        val (key1, value1) = "key1" to "value1"
        val (key2, value2) = "key2" to "value2"
        val (key3, value3) = "key3" to "value3"
        val (key4, _) = "key4" to "value4"

        // del, set, get
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

        // unlink
        run {
            unlink(key1)
            assertEquals(null, get(key1))
            set(key1, value1)
            assertEquals(value1, get(key1))
            unlink(key1)
            assertEquals(null, get(key1))
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

        // rename
        run {
            del(key1)
            del(key2)
            set(key1, value1)
            assertEquals(value1 to null, get(key1) to get(key2))
            rename(key1, key2)
            assertEquals(null to value1, get(key1) to get(key2))
        }

        // rename overwritten
        run {
            del(key1)
            del(key2)
            set(key1, value1)
            set(key2, value2)
            assertEquals(value1 to value2, get(key1) to get(key2))
            rename(key1, key2)
            assertEquals(null to value1, get(key1) to get(key2))
        }

        // rename source doesn't exists
        run {
            del(key1).also { del(key2) }
            expectException<RedisException>("ERR no such key processing 'RENAME'") { rename(key1, key2) }
        }

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
    fun testDumpRestore() = redisTest {
        // dump/restore
        run {
            val mykey = "mykey"
            val myvalue = "10"
            set(mykey, myvalue)
            val mykeyDump = dump(mykey)
            println(mykeyDump!!.size)
            expectException<RedisException>("BUSYKEY Target key name already exists. processing 'RESTORE'") {
                restore(mykey, mykeyDump)
            }
            del(mykey)
            assertEquals("OK", restore(mykey, mykeyDump))
            assertEquals(myvalue, get(mykey))
        }
    }

    @Test
    fun testGeo() = redisTest {
        geoTools {
            val key = "Sicily"
            del(key)
            assertEquals(
                2,
                geoadd(
                    key,
                    "Palermo" to GeoPosition(13.361389, 38.115556),
                    "Catania" to GeoPosition(15.087269, 37.502669)
                )
            )
            assertEquals(166274.1516, geodist(key, "Palermo", "Catania"))
            assertEquals(166.2742, geodist(key, "Palermo", "Catania", GeoUnit.KILOMETERS))
            assertEquals(listOf("sqc8b49rny0", "sqdtr74hyu0"), geohash(key, "Palermo", "Catania"))
            assertEquals(listOf(), geohash(key))
            assertEquals(
                listOf(
                    GeoPosition(13.361389338970184, 38.1155563954963),
                    null,
                    GeoPosition(15.087267458438873, 37.50266842333162)
                ), geopos(key, "Palermo", "NonExisting", "Catania")
            )

            assertEquals(
                listOf(
                    GeoRadiusResult(name = "Palermo", distance = 190.4424.kilometers, coords = null, hash = null),
                    GeoRadiusResult(name = "Catania", distance = 56.4413.kilometers, coords = null, hash = null)
                ), georadius(key, GeoPosition(15, 37), 200.kilometers, withDist = true)
            )

            assertEquals(
                listOf(
                    GeoRadiusResult(
                        "Palermo", 190.4424.kilometers, GeoPosition(13.361389338970184, 38.1155563954963), 3479099956230698
                    ),
                    GeoRadiusResult(
                        "Catania", 56.4413.kilometers, GeoPosition(15.087267458438873, 37.50266842333162), 3479447370796909
                    )
                ), georadius(key, GeoPosition(15, 37), 200.kilometers, withDist = true, withCoord = true, withHash = true)
            )

            assertEquals(
                listOf(
                    GeoRadiusResult("Catania", 166.2742.kilometers, GeoPosition(15.087267458438873, 37.50266842333162))
                ), georadiusbymember(
                    key, "Palermo", 200.kilometers, withDist = true, withCoord = true, withHash = false,
                    sort = SortDirection.DESC, count = 1L
                )
            )

            assertEquals(
                listOf(
                    GeoRadiusResult("Palermo", 0.0.kilometers, GeoPosition(13.361389338970184, 38.1155563954963))
                ), georadiusbymember(
                    key, "Palermo", 200.kilometers, withDist = true, withCoord = true, withHash = false,
                    sort = SortDirection.ASC, count = 1L
                )
            )
        }
    }

    @Test
    fun testConnection() = redisTest {
        assertEquals("hello", echo("hello"))
        assertEquals("PONG", ping())
        assertEquals("hello", ping("hello"))

        select(1).set("key", "value1")
        select(2).set("key", "value2")

        assertEquals("value1", select(1).get("key"))
        assertEquals("value2", select(2).get("key"))

        swapdb(1, 2)

        assertEquals("value2", select(1).get("key"))
        assertEquals("value1", select(2).get("key"))

        quit()
        //run {
        //    println(clientList())
        //}
    }

    @Test
    fun testHyperlog() = redisTest {
        val hll1 = "hll1"
        val hll2 = "hll2"
        val hll3 = "hll3"
        del(hll1)
        del(hll2)
        assertEquals(1, pfadd(hll1, "foo", "bar", "zap", "a"))
        assertEquals(1, pfadd(hll2, "a", "b", "c", "foo"))
        assertEquals(Unit, pfmerge(hll3, hll1, hll2))
        assertEquals(6, pfcount(hll3))
    }

    private val FLUSHABLE_DB = 10
    private val FLUSHABLE_DB2 = 11

    @Test
    fun testScan() = redisTest {
        // scan
        run {
            select(FLUSHABLE_DB)
            flushdb()
            for (n in 0 until 100) set("key$n", "value$n")
            assertEquals((0 until 100).map { "key$it" }.sorted(), scan().toList().sorted())
        }
        // hscan
        run {
            select(FLUSHABLE_DB)
            flushdb()
            val key = "key"
            for (n in 0 until 100) hset(key, "key$n", "value$n")
            val items = hscan(key).toList()
            assertEquals((0 until 100).map { "key$it" }.sorted(), items.map { it.first }.sorted())
            assertEquals((0 until 100).map { "value$it" }.sorted(), items.map { it.second }.sorted())
        }
        // sscan
        run {
            select(FLUSHABLE_DB)
            flushdb()
            val key = "key"
            for (n in 0 until 100) sadd(key, "key$n")
            assertEquals((0 until 100).map { "key$it" }.sorted(), sscan(key).toList().sorted())
        }
        // zscan
        run {
            select(FLUSHABLE_DB)
            flushdb()
            val key = "key"
            for (n in 0 until 100) zadd(key, "key$n", n.toDouble())
            val items = zscan(key).toList()
            assertEquals((0 until 100).map { "key$it" }.sorted(), items.map { it.first }.sorted())
            assertEquals((0 until 100).map { it.toDouble() }.sorted(), items.map { it.second }.sorted())
        }
    }

    @Test
    fun testKeysMove() = redisTest {
        val (db1, db2) = FLUSHABLE_DB to FLUSHABLE_DB2
        val (key1, value1) = "key1" to "value1"
        select(db1).flushdb()
        select(db2).flushdb()
        select(db1)
        set(key1, value1)
        move(key1, db2)
        assertEquals(null, select(db1).get(key1))
        assertEquals(value1, select(db2).get(key1))
    }

    @Test
    fun testServer() = redisTest {
        // 60 seconds of difference. Since this is a docker instance, time should be synchronized with the host
        assertThat(abs(Date().time - time().first.time), lessThan(60_000L))

        save()
        bgsave()

        // Saved right now
        assertThat(abs(Date().time - lastsave().time), lessThan(60_000L))

        clientSetname("ktor-client-redis")
        assertEquals("ktor-client-redis", clientGetname())

        // command, command count, command getkeys
        run {
            val cmds = command().associateBy { it.name }
            assertThat(cmds.size, greaterThan(150))
            assertEquals(
                cmds["set"],
                CommandInfo(
                    name = "set", arity = -3, flags = setOf("write", "denyoom"), firstKey = 1, lastKey = 1, step = 1
                )
            )
            val setCmd = cmds["set"]!!

            assert(setCmd.hasWrite)
            assert(setCmd.hasDenyoom)
            assert(!setCmd.hasAdmin)

            assertEquals(cmds.size, commandCount())
            assertEquals(listOf("a", "c", "e"), commandGetKeys("mset", "a", "b", "c", "d", "e", "f"))
        }
        // command info
        run {
            assertEquals(
                listOf(
                    CommandInfo(
                        name = "set", arity = -3, flags = setOf("write", "denyoom"), firstKey = 1, lastKey = 1, step = 1
                    ),
                    CommandInfo(
                        name = "get", arity = 2, flags = setOf("readonly", "fast"), firstKey = 1, lastKey = 1, step = 1
                    )
                ),
                commandInfo("set", "get")
            )
        }
    }

    @Test
    fun testStream() = redisTest {
        val mystream = "mystream"
        del(mystream)

        val id = xadd(mystream, "name" to "Sara", "surname" to "OConnor")
        assertEquals(1, xlen(mystream))
        Regex("(\\d+)-(\\d+)").matchEntire(id).apply {
            assertThat(abs(Date().time - this!!.groupValues[1].toLong()), lessThan(60_000L))
            assertEquals("0", this!!.groupValues[2])
        }
        assertEquals(
            id to mapOf("name" to "Sara", "surname" to "OConnor"),
            xget(mystream, id)
        )
        xdel(mystream, id)
        assertEquals(0, xlen(mystream))

        run {
            del(mystream)
            val firstId = xadd(mystream, "name" to "a")
            xadd(mystream, "name" to "b")
            xadd(mystream, "name" to "c")
            xadd(mystream, "name" to "d")
            val lastId = xadd(mystream, "name" to "e")
            assertEquals(
                listOf(
                    mapOf("name" to "a"),
                    mapOf("name" to "b"),
                    mapOf("name" to "c"),
                    mapOf("name" to "d"),
                    mapOf("name" to "e")
                ),
                xrange(mystream).map { it.value }.toList()
            )
            assertEquals(
                listOf(
                    mapOf("name" to "a"),
                    mapOf("name" to "b"),
                    mapOf("name" to "c"),
                    mapOf("name" to "d"),
                    mapOf("name" to "e")
                ),
                xrangeChannel(mystream, chunkSize = 2).map { it.second }.toList()
            )
            println(xinfoHelp())
            val info = xinfoStream(mystream)
            assertEquals(5, info.length)
            assertEquals(0, info.groups)
            assertEquals(lastId, info.lastGeneratedId)
            assertEquals(firstId, info.firstEntry?.first)
            assertEquals(lastId, info.lastEntry?.first)
            assertEquals(mapOf("name" to "a"), info.firstEntry?.second)
            assertEquals(mapOf("name" to "e"), info.lastEntry?.second)
            println("xinfoGroups(mystream):")
            println(xinfoGroups(mystream))

        }
    }

    @Test
    fun testStreamConsumer() = redisTest {
        val mystream = "mystream"
        val mygroup1 = "mygroup1"
        val consumer1 = "consumer1"
        del(mystream)
        println(xgroupHelp().joinToString("\n"))
        xcreate(mystream)
        xgroupCreate(mystream, mygroup1)

        val log = arrayListOf<String>()

        xadd(mystream, "name" to "a")
        val process1 = async {
            RedisClient(address, password = REDIS_PASSWORD).apply {
                xprocessBatch(mystream, mygroup1, consumer1, blockMs = 1_000, batchSize = 3, id = ">") {
                        stream, id, info ->
                    log += "$stream :: $info"
                    println("$stream :: $info")
                }
            }
        }
        xadd(mystream, "name" to "b")
        xadd(mystream, "name" to "c")
        process1.await()
        assertEquals(
            listOf(
                "mystream :: {name=a}",
                "mystream :: {name=b}",
                "mystream :: {name=c}"
            ),
            log
        )
    }

    @Test
    fun testServerConfig() = redisTest {
        val conf = configGet()
        for ((k, v) in conf) println("$k:$v")
        assertThat(conf.size, greaterThan(50))
        assertEquals("6379", conf["port"])
        assertEquals(mapOf("port" to "6379"), configGet("port"))
    }

    @Test
    fun testBitfield() = redisTest {
        val key1 = "key1"
        del(key1)
        val results = bitfield(key1) {
            val i4 = i(4)
            set(i4, 0, 1)
            incrby(i4, 0, +1)
            overflowSaturate()
            incrby(i4, 0, +100)
            get(i4, 0)
            incrby(i4, 0, -100)
        }
        assertEquals(
            listOf(0L, 2L, 7L, 7L, -8L),
            results
        )
    }

    @Test
    fun testSort() = redisTest {
        // list sort
        run {
            del(key1)
            lpush(key1, "c", "a", "b")
            assertEquals(listOf("a", "b", "c"), sort(key1, alpha = true).items)
        }
        // set sort
        run {
            del(key1)
            sadd(key1, "c", "a", "b")
            assertEquals(listOf("a", "b", "c"), sort(key1, alpha = true).items)
        }
        // sorted set sort
        run {
            del(key1)
            zadd(key1, "c" to 1.0, "a" to 2.0, "b" to 0.0)
            assertEquals(listOf("a", "b", "c"), sort(key1, alpha = true).items)
        }
        // descending sort
        run {
            del(key1)
            lpush(key1, "c", "a", "b")
            assertEquals(listOf("c", "b", "a"), sort(key1, alpha = true, sortDirection = -1).items)
        }
        // range
        run {
            del(key1)
            lpush(key1, "c", "a", "b")
            assertEquals(listOf("b", "c"), sort(key1, alpha = true, sortDirection = +1, range = 1L..2L).items)
        }
        // storeDestination
        run {
            del(key1)
            del(key2)
            lpush(key1, "c", "a", "b")
            val result = sort(key1, storeDestination = key2)
            assertEquals(3L, result.count)
            assertEquals(null, result.items)
            assertEquals(listOf("a", "b", "c"), lgetall(key2))
        }
    }

    @Test
    fun testSortedSetBoolOps() = redisTest {
        suspend fun prepare(aggregate: RedisZBoolStoreAggregate) {
            del(key1, key2, key3, key4)
            zadd(key1, "a" to 100.0, "b" to 50.0, "c" to 300.0)
            zadd(key2, "a" to 10.0, "c" to 0.0, "d" to 1000.0)
            zunionstore(key3, key1, key2, aggregate = aggregate)
            zinterstore(key4, key1, key2, aggregate = aggregate)
        }
        // sum
        run {
            prepare(RedisZBoolStoreAggregate.SUM)
            assertEquals(mapOf("a" to 110.0, "b" to 50.0, "c" to 300.0, "d" to 1000.0), zgetall(key3).toList().sortedBy { it.first }.toMap())
            assertEquals(mapOf("a" to 110.0, "c" to 300.0), zgetall(key4).toList().sortedBy { it.first }.toMap())
        }
        // min
        run {
            prepare(RedisZBoolStoreAggregate.MIN)
            assertEquals(mapOf("a" to 10.0, "b" to 50.0, "c" to 0.0, "d" to 1000.0), zgetall(key3).toList().sortedBy { it.first }.toMap())
            assertEquals(mapOf("a" to 10.0, "c" to 0.0), zgetall(key4).toList().sortedBy { it.first }.toMap())
        }
    }

    @Test
    fun testDisableProcessing() = redisTest {
        RedisClient(address, maxConnections = 1, password = REDIS_PASSWORD).apply {
            clientReplyOff {
                del(key1)
                lpush(key1, "a", "b", "c")
            }
            assertEquals(listOf("a", "b", "c"), lgetall(key1).sorted())
        }
    }

    @Test
    fun testMonitor() = redisTest {
        val log = arrayListOf<String>()
        val prepared = CompletableDeferred<Unit>()
        val complete = CompletableDeferred<Unit>()
        launch(start = CoroutineStart.UNDISPATCHED) {
            RedisClient(address, maxConnections = 1, password = REDIS_PASSWORD).apply {
                val channel = monitor()
                prepared.complete(Unit)
                repeat(4) {
                    log += channel.receive()
                }
                complete.complete(Unit)
            }
        }
        val value1 = "value1"
        prepared.await()
        RedisClient(address, maxConnections = 1, password = REDIS_PASSWORD).apply {
            del(key1)
            set(key1, value1)
            assertEquals(value1, get(key1))
            complete.await()
            assertEquals(
                listOf(
                    "'AUTH' '$REDIS_PASSWORD'",
                    "'DEL' 'key1'",
                    "'SET' 'key1' 'value1'",
                    "'GET' 'key1'"
                ),
                log.map {
                    it.replace(Regex("^\\d+\\.\\d+\\s*\\[.*?\\]\\s*"), "").replace('"', '\'')
                }
            )
        }
    }

    @Test
    fun testPubsub() = redisTest {
        val log = arrayListOf<RedisPubSub.Message>()
        val completed = CompletableDeferred<Unit>()
        val listening = CompletableDeferred<Unit>()
        launch(start = CoroutineStart.UNDISPATCHED) {
            RedisClient(address, maxConnections = 1, password = REDIS_PASSWORD).apply {
                val sub = subscribe("mypubsub")
                val messages = sub.messagesChannel()
                listening.complete(Unit)
                repeat(3) {
                    log += messages.receive()
                }
                completed.complete(Unit)
            }
        }

        launch(start = CoroutineStart.UNDISPATCHED) {
            listening.await()
            publish("mypubsub2", "nope")
            publish("mypubsub", "hi")
            publish("mypubsub", "hello")
            publish("mypubsub", "world")
            assertEquals(
                listOf(
                    RedisPubSub.Message("mypubsub", "hi"),
                    RedisPubSub.Message("mypubsub", "hello"),
                    RedisPubSub.Message("mypubsub", "world")
                ),
                log
            )
            completed.await()
        }
    }

    @Test
    fun testTransaction() = redisTest(maxConnections = 1) {
        val value = "value"
        del(key1)
        del(key2)
        transaction {
            set(key1, value)
            assertEquals("QUEUED", get(key1)) // The same client sees QUEUED
            redisTest(maxConnections = 1) {
                assertEquals(null, get(key1)) // Other clients doesn't sees the changes
            }
            set(key2, value)
        }
        assertEquals(value, get(key1)) // The same client now sees the result
        redisTest(maxConnections = 1) {
            assertEquals(value, get(key1)) // As does other clients
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

    inline fun <reified T : Throwable> expectException(message: String? = null, callback: () -> Unit) {
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
        maxConnections: Int = 50,
        cleanup: suspend Redis.() -> Unit = {},
        callback: suspend Redis.() -> Unit
    ) =
        redisTest(address, password, maxConnections = maxConnections) {
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