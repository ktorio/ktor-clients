[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [sort](./sort.md)

# sort

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.sort(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pattern: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, range: `[`LongRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-long-range/index.html)`? = null, vararg getPatterns: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sortDirection: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, alpha: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, storeDestination: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`RedisSortResult`](-redis-sort-result/index.md)

Returns or stores the elements contained in the list, set or sorted set at key.
By default, sorting is numeric and elements are compared by their value interpreted as double precision
floating point number. This is SORT in its simplest form: SORT mylist

https://redis.io/commands/sort

### Parameters

`alpha` - Set to order string elements lexicographically (required if elements in the list are not numbers)

**Since**
1.0.0

