[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [monitor](./monitor.md)

# monitor

`inline suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.monitor(): ReceiveChannel<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Streams back every command processed by the Redis server.
It can help in understanding what is happening to the database.
This command can both be used via redis-cli and via telnet.

https://redis.io/commands/monitor

**Since**
1.0.0

