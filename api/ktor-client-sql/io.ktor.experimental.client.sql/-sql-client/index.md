[ktor-client-sql](../../index.md) / [io.ktor.experimental.client.sql](../index.md) / [SqlClient](./index.md)

# SqlClient

`interface SqlClient : CoroutineScope, Closeable`

### Functions

| Name | Summary |
|---|---|
| [connection](connection.md) | `abstract suspend fun connection(): `[`SqlConnection`](../-sql-connection/index.md) |
| [execute](execute.md) | `abstract suspend fun execute(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlQueryResult`](../-sql-query-result.md) |
| [prepare](prepare.md) | `abstract suspend fun prepare(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlStatement`](../-sql-statement/index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [connection](../connection.md) | `suspend fun <R> `[`SqlClient`](./index.md)`.connection(block: suspend (connection: `[`SqlConnection`](../-sql-connection/index.md)`) -> `[`R`](../connection.md#R)`): `[`R`](../connection.md#R) |
| [select](../select.md) | `suspend fun `[`SqlClient`](./index.md)`.select(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlTables`](../-sql-tables/index.md) |
