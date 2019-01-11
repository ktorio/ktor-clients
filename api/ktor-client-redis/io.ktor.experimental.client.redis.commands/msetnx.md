[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [msetnx](./msetnx.md)

# msetnx

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.msetnx(vararg pairs: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.msetnx(map: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Set multiple keys to multiple values, only if none of the keys exist

https://redis.io/commands/msetnx

**Since**
1.0.1

