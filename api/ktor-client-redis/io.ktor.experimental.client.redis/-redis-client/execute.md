[ktor-client-redis](../../index.md) / [io.ktor.experimental.client.redis](../index.md) / [RedisClient](index.md) / [execute](./execute.md)

# execute

`suspend fun execute(vararg args: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`

Overrides [Redis.execute](../-redis/execute.md)

Executes a raw command. Each [args](../-redis/execute.md#io.ktor.experimental.client.redis.Redis$execute(kotlin.Array((kotlin.Any)))/args) will be sent as a String.

It returns a type depending on the command.
The returned value can be of type [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) or [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html).

It may throw a [RedisResponseException](#)

