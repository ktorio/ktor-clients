[ktor-client-sql](../../index.md) / [io.ktor.experimental.client.sql](../index.md) / [SqlConnection](./index.md)

# SqlConnection

`interface SqlConnection : CoroutineScope, Closeable`

### Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | `abstract suspend fun execute(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlQueryResult`](../-sql-query-result.md) |
| [prepare](prepare.md) | `abstract suspend fun prepare(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlStatement`](../-sql-statement/index.md) |
