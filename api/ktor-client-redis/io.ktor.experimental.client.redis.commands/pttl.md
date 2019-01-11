[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [pttl](./pttl.md)

# pttl

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.pttl(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

This commands returns the remaining time in milliseconds to live of a key that has an expire set.

A values less than 0, means an error.

https://redis.io/commands/pttl

**Since**
2.6.0

