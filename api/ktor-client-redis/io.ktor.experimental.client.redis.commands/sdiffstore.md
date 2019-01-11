[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [sdiffstore](./sdiffstore.md)

# sdiffstore

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.sdiffstore(destination: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Subtract multiple sets and store the resulting set in a key

https://redis.io/commands/sdiffstore

**Since**
1.0.0

