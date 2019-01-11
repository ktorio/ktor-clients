[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [lgetall](./lgetall.md)

# lgetall

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.lgetall(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Returns the whole list. (Shortcut of lrange(key, 0L until 4294967295L))

