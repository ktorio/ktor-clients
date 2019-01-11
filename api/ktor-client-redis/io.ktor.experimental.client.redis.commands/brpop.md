[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [brpop](./brpop.md)

# brpop

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.brpop(vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, timeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?`

Remove and get the last element in a list, or block until one is available

https://redis.io/commands/brpop

**Since**
2.0.0

