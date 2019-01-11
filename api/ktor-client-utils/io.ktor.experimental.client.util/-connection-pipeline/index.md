[ktor-client-utils](../../index.md) / [io.ktor.experimental.client.util](../index.md) / [ConnectionPipeline](./index.md)

# ConnectionPipeline

`abstract class ConnectionPipeline<TRequest : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, TResponse : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : CoroutineScope`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ConnectionPipeline(source: ReceiveChannel<`[`PipelineElement`](../-pipeline-element/index.md)`<`[`TRequest`](index.md#TRequest)`, `[`TResponse`](index.md#TResponse)`>>, pipelineSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 10, coroutineContext: <ERROR CLASS>)` |

### Properties

| Name | Summary |
|---|---|
| [coroutineContext](coroutine-context.md) | `open val coroutineContext: <ERROR CLASS>` |
| [reader](reader.md) | `val reader: SendChannel<`[`RequestContext`](../-request-context/index.md)`<`[`TResponse`](index.md#TResponse)`>>` |
| [writer](writer.md) | `val writer: Job` |

### Functions

| Name | Summary |
|---|---|
| [onDone](on-done.md) | `open fun onDone(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onError](on-error.md) | `open fun onError(cause: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStart](on-start.md) | `open suspend fun onStart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [receive](receive.md) | `abstract suspend fun receive(callScope: CoroutineScope): `[`TResponse`](index.md#TResponse) |
| [send](send.md) | `abstract suspend fun send(callScope: CoroutineScope, request: `[`TRequest`](index.md#TRequest)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
