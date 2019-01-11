[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [sdiff](./sdiff.md)

# sdiff

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.sdiff(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Subtract multiple sets

Returns the members of the set resulting from the difference between the first set and all the successive sets.

https://redis.io/commands/sdiff

**Since**
1.0.0

