[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [clientKill](./client-kill.md)

# clientKill

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.clientKill(clientId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, addr: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, skipme: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Kill the connection of a client

https://redis.io/commands/client-kill

### Parameters

`type` - Possible values: normal, master, slave or pubsub

**Since**
2.4.0

