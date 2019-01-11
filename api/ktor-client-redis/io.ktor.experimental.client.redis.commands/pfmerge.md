[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [pfmerge](./pfmerge.md)

# pfmerge

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.pfmerge(destKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg sourceKeys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Merge N different HyperLogLogs into a single one.

https://redis.io/commands/pfmerge

**Since**
2.8.9

