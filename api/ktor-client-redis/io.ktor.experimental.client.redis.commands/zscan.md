[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zscan](./zscan.md)

# zscan

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zscan(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): ReceiveChannel<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>>`

Incrementally iterate sorted sets elements and associated scores

https://redis.io/commands/zscan

**Since**
2.8.0

