[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zrangebyscore](./zrangebyscore.md)

# zrangebyscore

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zrangebyscore(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, min: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`, max: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`, limit: `[`LongRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-long-range/index.html)`? = null): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>`

Return a range of members in a sorted set, by score

https://redis.io/commands/zrangebylex

**Since**
1.0.5

