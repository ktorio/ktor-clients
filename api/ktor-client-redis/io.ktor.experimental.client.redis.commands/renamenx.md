[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [renamenx](./renamenx.md)

# renamenx

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.renamenx(oldKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, newKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Renames oldKey to newKey.

* If oldKey doesn't exists, it returns an error.
* If newkey already exists, it is NOT overwritten, and the function returns false.

https://redis.io/commands/renamenx

**Since**
1.0.0

