[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [save](./save.md)

# save

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.save(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Performs a synchronous save of the dataset producing a point in time snapshot of all the data inside
the Redis instance, in the form of an RDB file.

https://redis.io/commands/save

**Since**
1.0.0

