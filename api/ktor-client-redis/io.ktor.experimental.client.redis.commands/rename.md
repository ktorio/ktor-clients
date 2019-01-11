[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [rename](./rename.md)

# rename

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.rename(oldKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, newKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Renames oldKey to newKey.

* If oldKey doesn't exists, it returns an error.
* If newkey already exists, it is overwritten.

https://redis.io/commands/rename

**Since**
1.0.0

