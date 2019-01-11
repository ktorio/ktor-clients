[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [pfcount](./pfcount.md)

# pfcount

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.pfcount(vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Return the approximated cardinality of the set(s) observed by the HyperLogLog at key(s).

https://redis.io/commands/pfcount

**Since**
2.8.9

