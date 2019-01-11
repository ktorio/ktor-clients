[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zcount](./zcount.md)

# zcount

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zcount(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, min: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)` = Double.NEGATIVE_INFINITY, max: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)` = Double.POSITIVE_INFINITY, includeMin: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, includeMax: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Count the members in a sorted set with scores within the given values

https://redis.io/commands/zcount

**Since**
1.2.0

