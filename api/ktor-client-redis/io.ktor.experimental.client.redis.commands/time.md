[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [time](./time.md)

# time

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.time(): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Date`](http://docs.oracle.com/javase/6/docs/api/java/util/Date.html)`, `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`>`

Returns the current server time as a two items lists: a Unix timestamp and the
amount of microseconds already elapsed in the current second.
Basically the interface is very similar to the one of the gettimeofday system call.

https://redis.io/commands/time

**Since**
2.6.0

