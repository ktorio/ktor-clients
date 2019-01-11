[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [objectFreq](./object-freq.md)

# objectFreq

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.objectFreq(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Returns the logarithmic access frequency counter of the object stored at the specified key.
This subcommand is available when maxmemory-policy is set to an LFU policy.

https://redis.io/commands/object

**Since**
2.2.3

