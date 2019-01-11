[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [role](./role.md)

# role

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.role(): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`

Provide information on the role of a Redis instance in the context of replication,
by returning if the instance is currently a master, slave, or sentinel.
The command also returns additional information about the state of the replication (if the role is master or slave)
or the list of monitored master names (if the role is sentinel).

https://redis.io/commands/role

**Since**
2.8.12

