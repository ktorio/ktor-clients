[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zrevrange](./zrevrange.md)

# zrevrange

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zrevrange(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, stop: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>`

Return a range of members in a sorted set, by index, with scores ordered from high to low (with scores)

https://redis.io/commands/zrevrange

**Since**
1.2.0

