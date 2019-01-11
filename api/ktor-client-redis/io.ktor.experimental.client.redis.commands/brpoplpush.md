[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [brpoplpush](./brpoplpush.md)

# brpoplpush

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.brpoplpush(src: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, dst: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, timeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Pop a value from a list, push it to another list and return it; or block until one is available

https://redis.io/commands/brpoplpush

**Since**
2.2.0

