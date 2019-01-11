[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [clusterFailover](./cluster-failover.md)

# clusterFailover

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.clusterFailover(mode: `[`RedisClusterFailOverMode`](-redis-cluster-fail-over-mode/index.md)` = RedisClusterFailOverMode.FORCE): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Forces a slave to perform a manual failover of its master.

https://redis.io/commands/cluster-failover

**Since**
3.0.0

