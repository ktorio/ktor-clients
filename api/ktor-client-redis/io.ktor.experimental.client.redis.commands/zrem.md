[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zrem](./zrem.md)

# zrem

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zrem(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, member: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zrem(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg members: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>`

Remove one or more members from a sorted set

https://redis.io/commands/zrem

**Since**
1.2.0

