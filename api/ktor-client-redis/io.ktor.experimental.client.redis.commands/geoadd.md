[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [geoadd](./geoadd.md)

# geoadd

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.geoadd(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg items: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`GeoPosition`](../io.ktor.experimental.client.redis.geo/-geo-position/index.md)`>): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Add one or more geospatial items in the geospatial index represented using a sorted set

https://redis.io/commands/geoadd

**Since**
3.2.0

