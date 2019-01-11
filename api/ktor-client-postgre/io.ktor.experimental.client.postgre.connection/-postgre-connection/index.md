[ktor-client-postgre](../../index.md) / [io.ktor.experimental.client.postgre.connection](../index.md) / [PostgreConnection](./index.md)

# PostgreConnection

`class PostgreConnection : SqlConnection`

### Properties

| Name | Summary |
|---|---|
| [coroutineContext](coroutine-context.md) | `val coroutineContext: <ERROR CLASS>` |

### Functions

| Name | Summary |
|---|---|
| [close](close.md) | `fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [execute](execute.md) | `suspend fun execute(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): SqlQueryResult` |
| [prepare](prepare.md) | `suspend fun prepare(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): SqlStatement` |
