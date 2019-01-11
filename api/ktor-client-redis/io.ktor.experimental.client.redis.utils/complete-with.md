[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis.utils](index.md) / [completeWith](./complete-with.md)

# completeWith

`inline fun <T> completeWith(deferred: CompletableDeferred<`[`T`](complete-with.md#T)`>, block: () -> `[`T`](complete-with.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Use [block](complete-with.md#io.ktor.experimental.client.redis.utils$completeWith(kotlinx.coroutines.CompletableDeferred((io.ktor.experimental.client.redis.utils.completeWith.T)), kotlin.Function0((io.ktor.experimental.client.redis.utils.completeWith.T)))/block) to complete [deferred](complete-with.md#io.ktor.experimental.client.redis.utils$completeWith(kotlinx.coroutines.CompletableDeferred((io.ktor.experimental.client.redis.utils.completeWith.T)), kotlin.Function0((io.ktor.experimental.client.redis.utils.completeWith.T)))/deferred), also handles [block](complete-with.md#io.ktor.experimental.client.redis.utils$completeWith(kotlinx.coroutines.CompletableDeferred((io.ktor.experimental.client.redis.utils.completeWith.T)), kotlin.Function0((io.ktor.experimental.client.redis.utils.completeWith.T)))/block) exceptions

