package io.ktor.experimental.client.redis

/**
 * Append a value to a key
 *
 * https://redis.io/commands/append
 *
 * @since 2.0.0
 */
suspend fun Redis.append(key: String, value: String): String? = commandString("append", key, value)

/**
 * Count set bits in a string
 *
 * https://redis.io/commands/bitcount
 *
 * @since 2.6.0
 */
suspend fun Redis.bitcount(key: String): String? = commandString("bitcount", key)

/**
 * Count set bits in a string
 *
 * https://redis.io/commands/bitcount
 *
 * @since 2.6.0
 */
suspend fun Redis.bitcount(key: String, start: Int, end: Int): String? = commandString("bitcount", key, start, end)

/**
 * Count set bits in a string
 *
 * https://redis.io/commands/bitcount
 *
 * @since 2.6.0
 */
suspend fun Redis.bitcount(key: String, range: LongRange): Long =
    commandLong("bitcount", key, *arrayOfNotNull(range?.start, range?.endInclusive))

class RedisBitFieldBuilder {
    val cmds = arrayListOf<Any?>()

    data class Type(val str: String)

    fun u(bits: Int) = Type("u$bits")
    fun i(bits: Int) = Type("i$bits")
    fun type(bits: Int, signed: Boolean = true) = if (signed) i(bits) else u(bits)

    fun get(type: Type, offset: Long) {
        cmds += "GET"
        cmds += type.str
        cmds += offset
    }

    fun set(type: Type, offset: Long, value: Long) {
        cmds += "SET"
        cmds += type.str
        cmds += offset
        cmds += value
    }

    fun incrby(type: Type, offset: Long, increment: Long) {
        cmds += "INCRBY"
        cmds += type.str
        cmds += offset
        cmds += increment
    }

    fun overflowWrap() {
        cmds += "OVERFLOW"
        cmds += "WRAP"
    }

    fun overflowSaturate() {
        cmds += "OVERFLOW"
        cmds += "SAT"
    }

    fun overflowFail() {
        cmds += "OVERFLOW"
        cmds += "FAIL"
    }
}

/**
 * Perform arbitrary bitfield integer operations on strings
 *
 * https://redis.io/commands/bitfield
 *
 * @since 3.2.0
 */
suspend fun Redis.bitfield(key: String, callback: RedisBitFieldBuilder.() -> Unit): List<Long?> {
    val builder = RedisBitFieldBuilder()
    callback(builder)
    return commandArrayAny(*((listOf("BITFIELD", key) + builder.cmds).toTypedArray())).map { (it as? Number?)?.toLong() }
}

enum class RedisBitop { AND, OR, XOR, NOT }

/**
 * Perform bitwise operations between strings
 *
 * https://redis.io/commands/bitop
 *
 * @since 2.6.0
 */
suspend fun Redis.bitop(op: RedisBitop, destKey: String, vararg srcKeys: String): Long =
    commandLong(op.name, destKey, *srcKeys)

/**
 * Perform an AND operation between two or more strings.
 *
 * https://redis.io/commands/bitop
 *
 * @since 2.6.0
 */
suspend fun Redis.bitopAnd(destKey: String, vararg srcKeys: String): Long = bitop(RedisBitop.AND, destKey, *srcKeys)

/**
 * Perform an OR operation between two or more strings.
 *
 * https://redis.io/commands/bitop
 *
 * @since 2.6.0
 */
suspend fun Redis.bitopOr(destKey: String, vararg srcKeys: String): Long = bitop(RedisBitop.OR, destKey, *srcKeys)


/**
 * Perform a XOR operation between two or more strings.
 *
 * https://redis.io/commands/bitop
 *
 * @since 2.6.0
 */
suspend fun Redis.bitopXor(destKey: String, vararg srcKeys: String): Long = bitop(RedisBitop.XOR, destKey, *srcKeys)

/**
 * Perform a NOT operation from one string.
 *
 * https://redis.io/commands/bitop
 *
 * @since 2.6.0
 */
suspend fun Redis.bitopNot(destKey: String, srcKey: String): Long = bitop(RedisBitop.NOT, destKey, srcKey)

/**
 * Find first bit set or clear in a string
 *
 * https://redis.io/commands/bitpos
 *
 * @since 2.8.7
 */
suspend fun Redis.bitpos(key: String, bit: Int, range: LongRange?): Long =
    commandLong("bitpos", key, bit, *arrayOfNotNull(range?.start, range?.endInclusive))

/**
 * Decrement the integer value of a key by one
 *
 * https://redis.io/commands/decr
 *
 * @since 1.0.0
 */
suspend fun Redis.decr(key: String): Long = commandLong("decr", key)

/**
 * Decrement the integer value of a key by the given number
 *
 * https://redis.io/commands/decrby
 *
 * @since 1.0.0
 */
suspend fun Redis.decrby(key: String, decrement: Long): Long = commandLong("decrby", key, decrement)

/**
 * Increment the integer value of a key by one
 *
 * https://redis.io/commands/incr
 *
 * @since 1.0.0
 */
suspend fun Redis.incr(key: String): Long = commandLong("incr", key)

/**
 * Increment the integer value of a key by the given amount
 *
 * https://redis.io/commands/incrby
 *
 * @since 1.0.0
 */
suspend fun Redis.incrby(key: String, increment: Long): Long = commandLong("incrby", key, increment)

/**
 * Increment the float value of a key by the given amount
 *
 * https://redis.io/commands/incrbyfloat
 *
 * @since 2.6.0
 */
suspend fun Redis.incrbyfloat(key: String, increment: Double): Double = commandDouble("incrbyfloat", key, increment)

/**
 * Get the value of a key
 *
 * https://redis.io/commands/get
 *
 * @since 2.6.0
 */
suspend fun Redis.get(key: String): String? = commandString("get", key)

/**
 * Returns the bit value at offset in the string value stored at key
 *
 * https://redis.io/commands/getbit
 *
 * @since 2.2.0
 */
suspend fun Redis.getbit(key: String, offset: Int): Int = commandInt("getbit", key, offset)

/**
 * Get a substring of the string stored at a key
 *
 * https://redis.io/commands/getrange
 *
 * @since 2.4.0
 */
suspend fun Redis.getrange(key: String, start: Int, end: Int): String? = commandString("getrange", key, start, end)

/**
 * Set the string value of a key
 *
 * https://redis.io/commands/set
 *
 * @since 1.0.0
 */
suspend fun Redis.set(key: String, value: String): Unit = commandUnit("set", key, value)

enum class RedisSetMode(val v: String?) {
    SET_ONLY_IF_NOT_EXISTS("NX"),
    SET_ONLY_IF_EXISTS("XX"),
    SET_ALWAYS(null);
}

/**
 * Set the string value of a key
 *
 * https://redis.io/commands/set
 *
 * @since 1.0.0
 */
suspend fun Redis.set(
    key: String, value: String, expirationMs: Long? = null, mode: RedisSetMode = RedisSetMode.SET_ALWAYS
): Unit = commandUnit(
    *((listOf("set", key, value) + (if (expirationMs != null) listOf("PX", expirationMs) else listOf()) + mode.v
            ).filterNotNull().toTypedArray())
)

/**
 * Set the string value of a key and return its old value
 *
 * https://redis.io/commands/getset
 *
 * @since 1.0.0
 */
suspend fun Redis.getset(key: String, value: String): String? = commandString("set", key, value)

/**
 * Set multiple keys to multiple values
 *
 * https://redis.io/commands/mget
 *
 * @since 1.0.0
 */
suspend fun Redis.mget(vararg keys: String): List<String?> = commandArrayStringNull("mget", *keys)

/**
 * Set multiple keys to multiple values
 *
 * https://redis.io/commands/mset
 *
 * @since 1.0.1
 */
suspend fun Redis.mset(vararg pairs: Pair<String, String>): Unit =
    commandUnit("mset", *pairs.flatMap { listOf(it.first, it.second) }.toTypedArray())

/**
 * Set multiple keys to multiple values
 *
 * https://redis.io/commands/mset
 *
 * @since 1.0.1
 */
suspend fun Redis.mset(map: Map<String, String>): Unit =
    commandUnit("mset", *map.flatMap { listOf(it.key, it.value) }.toTypedArray())

/**
 * Set multiple keys to multiple values, only if none of the keys exist
 *
 * https://redis.io/commands/msetnx
 *
 * @since 1.0.1
 */
suspend fun Redis.msetnx(vararg pairs: Pair<String, String>): Boolean =
    commandBool("mset", *pairs.flatMap { listOf(it.first, it.second) }.toTypedArray())

/**
 * Set multiple keys to multiple values, only if none of the keys exist
 *
 * https://redis.io/commands/msetnx
 *
 * @since 1.0.1
 */
suspend fun Redis.msetnx(map: Map<String, String>): Boolean =
    commandBool("mset", *map.flatMap { listOf(it.key, it.value) }.toTypedArray())

/**
 * Set the value of a key, only if the key does not exist
 *
 * https://redis.io/commands/setnx
 *
 * @since 1.0.0
 */
suspend fun Redis.setnx(key: String, value: String): Boolean = commandBool("setnx", key, value)

/**
 * Set the value and expiration of a key
 *
 * https://redis.io/commands/setex
 *
 * @since 2.0.0
 */
suspend fun Redis.setex(key: String, seconds: Long, value: String): Unit = commandUnit("setex", key, seconds, value)

/**
 * Set the value and expiration in milliseconds of a key
 *
 * https://redis.io/commands/psetex
 *
 * @since 2.6.0
 */
suspend fun Redis.psetex(key: String, ms: Long, value: String): Unit = commandUnit("psetex", key, ms, value)

/**
 * Sets or clears the bit at offset in the string value stored at key
 *
 * https://redis.io/commands/setbit
 *
 * @since 2.2.0
 */
suspend fun Redis.setbit(key: String, offset: Int, value: Boolean): Unit =
    commandUnit("setbit", key, offset, if (value) 1 else 0)

/**
 * Overwrite part of a string at key starting at the specified offset
 *
 * https://redis.io/commands/setrange
 *
 * @since 2.2.0
 */
suspend fun Redis.setrange(key: String, offset: Int, value: String): Long = commandLong("setrange", key, offset, value)

/**
 * Get the length of the value stored in a key
 *
 * https://redis.io/commands/strlen
 *
 * @since 2.2.0
 */
suspend fun Redis.strlen(key: String): Long = commandLong("strlen", key)
