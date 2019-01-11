[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [touch](./touch.md)

# touch

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.touch(vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Alters the last access time of a key(s). A key is ignored if it does not exist.

https://redis.io/commands/touch

**Since**
3.2.1

