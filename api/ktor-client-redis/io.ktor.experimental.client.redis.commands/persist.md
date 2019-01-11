[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [persist](./persist.md)

# persist

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.persist(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Remove the existing timeout on key, turning the key from volatile (a key with an expire set)
to persistent (a key that will never expire as no timeout is associated).

https://redis.io/commands/persist

**Since**
2.2.0

