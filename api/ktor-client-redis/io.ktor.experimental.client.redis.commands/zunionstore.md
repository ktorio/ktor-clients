[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [zunionstore](./zunionstore.md)

# zunionstore

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zunionstore(dest: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg keysWithScores: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>, aggregate: `[`RedisZBoolStoreAggregate`](-redis-z-bool-store-aggregate/index.md)` = RedisZBoolStoreAggregate.SUM): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)
`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.zunionstore(dest: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, aggregate: `[`RedisZBoolStoreAggregate`](-redis-z-bool-store-aggregate/index.md)` = RedisZBoolStoreAggregate.SUM): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Add multiple sorted sets and store the resulting sorted set in a new key

https://redis.io/commands/zunionstore

**Since**
2.0.0

