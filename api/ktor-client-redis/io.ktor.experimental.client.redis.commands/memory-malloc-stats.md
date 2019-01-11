[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [memoryMallocStats](./memory-malloc-stats.md)

# memoryMallocStats

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.memoryMallocStats(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Provides an internal statistics report from the memory allocator.

This command is currently implemented only when using jemalloc as an allocator,
and evaluates to a benign NOOP for all others.

https://redis.io/commands/memory-malloc-stats

**Since**
4.0.0

