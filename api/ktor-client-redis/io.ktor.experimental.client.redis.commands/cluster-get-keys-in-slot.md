[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [clusterGetKeysInSlot](./cluster-get-keys-in-slot.md)

# clusterGetKeysInSlot

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.clusterGetKeysInSlot(slot: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, count: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Return local key names in the specified hash slot

https://redis.io/commands/cluster-getkeysinslot

**Since**
3.0.0

