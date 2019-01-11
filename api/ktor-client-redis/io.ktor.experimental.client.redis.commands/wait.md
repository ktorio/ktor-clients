[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [wait](./wait.md)

# wait

`suspend fun `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.wait(numslaves: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, timeoutMs: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

This command blocks the current client until all the previous write commands are successfully
transferred and acknowledged by at least the specified number of slaves.
If the timeout, specified in milliseconds, is reached, the command returns even
if the specified number of slaves were not yet reached.

https://redis.io/commands/wait

**Since**
3.0.0

