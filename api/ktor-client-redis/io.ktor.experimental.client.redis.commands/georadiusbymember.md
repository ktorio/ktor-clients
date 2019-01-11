[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [georadiusbymember](./georadiusbymember.md)

# georadiusbymember

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.georadiusbymember(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, member: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, radius: `[`GeoDistance`](../io.ktor.experimental.client.redis.geo/-geo-distance/index.md)`, withCoord: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withDist: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withHash: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, count: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, sort: `[`SortDirection`](-sort-direction/index.md)`? = null, storeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, storeDistKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GeoRadiusResult`](-geo-radius-result/index.md)`>`

Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a member

https://redis.io/commands/georadiusbymember

**Since**
3.2.0

