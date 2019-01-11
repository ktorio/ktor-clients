[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [pexpire](./pexpire.md)

# pexpire

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.pexpire(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, ms: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

This command works exactly like EXPIRE but the time to live
of the key is specified in milliseconds instead of seconds.

https://redis.io/commands/pexpire

**Since**
2.6.0

