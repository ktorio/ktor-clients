[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [bitcount](./bitcount.md)

# bitcount

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.bitcount(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.bitcount(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, start: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, end: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.bitcount(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, range: `[`LongRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-long-range/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Count set bits in a string

https://redis.io/commands/bitcount

**Since**
2.6.0

