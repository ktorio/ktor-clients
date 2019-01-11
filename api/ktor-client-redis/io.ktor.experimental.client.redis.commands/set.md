[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [set](./set.md)

# set

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.set(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.set(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, expirationMs: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, mode: `[`RedisSetMode`](-redis-set-mode/index.md)` = RedisSetMode.SET_ALWAYS): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Set the string value of a key

https://redis.io/commands/set

**Since**
1.0.0

