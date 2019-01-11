[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [memoryPurge](./memory-purge.md)

# memoryPurge

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.memoryPurge(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

Attempts to purge dirty pages so these can be reclaimed by the allocator.

This command is currently implemented only when using jemalloc as an allocator,
and evaluates to a benign NOOP for all others.

https://redis.io/commands/memory-purge

**Since**
4.0.0

