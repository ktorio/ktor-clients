[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [srandmember](./srandmember.md)

# srandmember

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.srandmember(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.srandmember(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, count: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Get one or multiple random members from a set

https://redis.io/commands/srandmember

**Since**
1.0.0

