[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [objectIdletime](./object-idletime.md)

# objectIdletime

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.objectIdletime(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Returns the number of seconds since the object stored at the specified key is idle
(not requested by read or write operations).
While the value is returned in seconds the actual resolution of this timer is 10 seconds,
but may vary in future implementations.
This subcommand is available when maxmemory-policy is set to an LRU policy or noeviction.

https://redis.io/commands/object

**Since**
2.2.3

