[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [restore](./restore.md)

# restore

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.restore(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, serializedValue: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`?, ttl: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0L, replace: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Create a key using the provided serialized value, previously obtained using DUMP.

https://redis.io/commands/restore

**Since**
2.6.0

