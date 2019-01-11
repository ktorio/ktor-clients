[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zremrangebylex](./zremrangebylex.md)

# zremrangebylex

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zremrangebylex(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, min: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, max: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Remove all members in a sorted set between the given lexicographical range

https://redis.io/commands/zremrangebylex

**Since**
2.8.9

