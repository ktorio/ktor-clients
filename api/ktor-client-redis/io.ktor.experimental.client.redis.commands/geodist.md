[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [geodist](./geodist.md)

# geodist

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.geodist(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, member1: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, member2: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, unit: `[`GeoUnit`](../io.ktor.experimental.client.redis.geo/-geo-unit/index.md)` = GeoUnit.METERS): `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`?`

Returns the distance between two members of a geospatial index

https://redis.io/commands/geodist

**Since**
3.2.0

