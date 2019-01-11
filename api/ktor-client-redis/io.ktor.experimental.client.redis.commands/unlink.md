[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [unlink](./unlink.md)

# unlink

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.unlink(vararg keys: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

This command is very similar to DEL: it removes the specified keys.
Just like DEL a key is ignored if it does not exist.
However the command performs the actual memory reclaiming in a different thread,
so it is not blocking, while DEL is.
This is where the command name comes from: the command just unlinks the keys from the keyspace.
The actual removal will happen later asynchronously.

https://redis.io/commands/unlink

**Since**
4.0.0

