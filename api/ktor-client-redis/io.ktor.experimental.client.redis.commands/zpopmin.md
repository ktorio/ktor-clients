[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zpopmin](./zpopmin.md)

# zpopmin

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zpopmin(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, count: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>`

Removes and returns up to count members with the lowest scores in the sorted set stored at key.

https://redis.io/commands/zpopmin

**Since**
5.0.0

