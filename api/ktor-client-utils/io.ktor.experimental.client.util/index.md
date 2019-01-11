[ktor-client-utils](../index.md) / [io.ktor.experimental.client.util](./index.md)

## Package io.ktor.experimental.client.util

### Types

| Name | Summary |
|---|---|
| [ConnectionPipeline](-connection-pipeline/index.md) | `abstract class ConnectionPipeline<TRequest : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, TResponse : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : CoroutineScope` |
| [ContextManager](-context-manager/index.md) | `class ContextManager : CoroutineScope` |
| [Hex](-hex/index.md) | `object Hex` |
| [PipelineElement](-pipeline-element/index.md) | `class PipelineElement<TRequest : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, TResponse : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |
| [RequestContext](-request-context/index.md) | `class RequestContext<TResponse : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |

### Exceptions

| Name | Summary |
|---|---|
| [PipelineException](-pipeline-exception/index.md) | `class PipelineException : `[`RuntimeException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.ByteArray](kotlin.-byte-array/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
| [kotlinx.coroutines.CompletableDeferred](kotlinx.coroutines.-completable-deferred/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [deferred](deferred.md) | `suspend fun <T> deferred(block: (CompletableDeferred<`[`T`](deferred.md#T)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`T`](deferred.md#T) |
| [silent](silent.md) | `fun silent(block: () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
