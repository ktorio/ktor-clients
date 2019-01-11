[ktor-client-utils](../../index.md) / [io.ktor.experimental.client.util](../index.md) / [RequestContext](./index.md)

# RequestContext

`class RequestContext<TResponse : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RequestContext(response: CompletableDeferred<`[`TResponse`](index.md#TResponse)`>, callContext: CoroutineScope)` |

### Properties

| Name | Summary |
|---|---|
| [callContext](call-context.md) | `val callContext: CoroutineScope` |
| [response](response.md) | `val response: CompletableDeferred<`[`TResponse`](index.md#TResponse)`>` |
