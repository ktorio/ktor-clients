[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [hscan](./hscan.md)

# hscan

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.hscan(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): ReceiveChannel<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>`

Incrementally iterate hash fields and associated values

https://redis.io/commands/hscan

**Since**
2.8.0

