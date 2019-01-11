[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [objectRefcount](./object-refcount.md)

# objectRefcount

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.objectRefcount(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Returns the number of references of the value associated with the specified key.
This command is mainly useful for debugging.

https://redis.io/commands/object

**Since**
2.2.3

