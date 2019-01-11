[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [swapdb](./swapdb.md)

# swapdb

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.swapdb(db1: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, db2: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)

This command swaps two Redis databases, so that immediately all the clients connected
to a given database will see the data of the other database, and the other way around.

https://redis.io/commands/swapdb

**Since**
4.0.0

