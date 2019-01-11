[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.commands](index.md) / [executeTyped](./execute-typed.md)

# executeTyped

`suspend fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.executeTyped(vararg args: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](execute-typed.md#T)`>): `[`T`](execute-typed.md#T)
`inline suspend fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Redis`](../io.ktor.experimental.client.redis/-redis/index.md)`.executeTyped(vararg args: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`T`](execute-typed.md#T)