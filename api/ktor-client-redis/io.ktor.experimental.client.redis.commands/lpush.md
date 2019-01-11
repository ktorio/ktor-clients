[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [lpush](./lpush.md)

# lpush

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.lpush(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg extraValues: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Insert all the specified values at the head of the list stored at key.

https://redis.io/commands/lpush

**Since**
1.0.0

