[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [type](./type.md)

# type

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.type(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Returns the string representation of the type of the value stored at key.
The different types that can be returned are: string, list, set, zset and hash.

https://redis.io/commands/type

**Since**
1.0.0

