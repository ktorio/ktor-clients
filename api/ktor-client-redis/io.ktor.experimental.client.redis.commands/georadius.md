[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [georadius](./georadius.md)

# georadius

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.georadius(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pos: `[`GeoPosition`](../io.ktor.experimental.client.redis.geo/-geo-position/index.md)`, radius: `[`GeoDistance`](../io.ktor.experimental.client.redis.geo/-geo-distance/index.md)`, withCoord: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withDist: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withHash: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, count: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, sort: `[`SortDirection`](-sort-direction/index.md)`? = null, storeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, storeDistKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GeoRadiusResult`](-geo-radius-result/index.md)`>`

Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a point

https://redis.io/commands/georadius

**Since**
3.2.0

