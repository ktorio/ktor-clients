[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zrangebylex](./zrangebylex.md)

# zrangebylex

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zrangebylex(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, min: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, max: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`LongRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-long-range/index.html)`? = null): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Return a range of members in a sorted set, by lexicographical range

https://redis.io/commands/zrangebylex

**Since**
2.8.9

