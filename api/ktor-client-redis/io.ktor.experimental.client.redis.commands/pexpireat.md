[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [pexpireat](./pexpireat.md)

# pexpireat

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.pexpireat(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, date: `[`Date`](http://docs.oracle.com/javase/6/docs/api/java/util/Date.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

PEXPIREAT has the same effect and semantic as EXPIREAT,
but the Unix time at which the key will expire is specified in milliseconds instead of seconds.

https://redis.io/commands/pexpireat

**Since**
2.6.0

