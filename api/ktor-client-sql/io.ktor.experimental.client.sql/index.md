[ktor-client-sql](../index.md) / [io.ktor.experimental.client.sql](./index.md)

## Package io.ktor.experimental.client.sql

### Types

| Name | Summary |
|---|---|
| [SqlBatchResult](-sql-batch-result/index.md) | `class SqlBatchResult<T : `[`SqlQueryResult`](-sql-query-result.md)`> : `[`SqlQueryResult`](-sql-query-result.md) |
| [SqlCell](-sql-cell/index.md) | `interface SqlCell : CoroutineScope` |
| [SqlClient](-sql-client/index.md) | `interface SqlClient : CoroutineScope, Closeable` |
| [SqlColumn](-sql-column/index.md) | `interface SqlColumn` |
| [SqlConnection](-sql-connection/index.md) | `interface SqlConnection : CoroutineScope, Closeable` |
| [SqlEmptyResult](-sql-empty-result.md) | `object SqlEmptyResult : `[`SqlQueryResult`](-sql-query-result.md) |
| [SqlMessage](-sql-message/index.md) | `class SqlMessage : `[`SqlQueryResult`](-sql-query-result.md) |
| [SqlQueryResult](-sql-query-result.md) | `interface SqlQueryResult` |
| [SqlRow](-sql-row/index.md) | `interface SqlRow : CoroutineScope` |
| [SqlStatement](-sql-statement/index.md) | `abstract class SqlStatement : `[`SqlQueryResult`](-sql-query-result.md) |
| [SqlTable](-sql-table/index.md) | `interface SqlTable : ReceiveChannel<`[`SqlRow`](-sql-row/index.md)`>, CoroutineScope` |
| [SqlTables](-sql-tables/index.md) | `abstract class SqlTables : `[`SqlQueryResult`](-sql-query-result.md)`, ReceiveChannel<`[`SqlTable`](-sql-table/index.md)`>` |
| [SqlType](-sql-type/index.md) | `enum class SqlType` |

### Exceptions

| Name | Summary |
|---|---|
| [ColumnNotFoundException](-column-not-found-exception/index.md) | `class ColumnNotFoundException : `[`IllegalArgumentException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) |
| [SqlQueryTypeMismatchException](-sql-query-type-mismatch-exception/index.md) | `class SqlQueryTypeMismatchException : `[`IllegalStateException`](http://docs.oracle.com/javase/6/docs/api/java/lang/IllegalStateException.html) |

### Functions

| Name | Summary |
|---|---|
| [connection](connection.md) | `suspend fun <R> `[`SqlClient`](-sql-client/index.md)`.connection(block: suspend (connection: `[`SqlConnection`](-sql-connection/index.md)`) -> `[`R`](connection.md#R)`): `[`R`](connection.md#R) |
| [get](get.md) | `operator fun `[`SqlRow`](-sql-row/index.md)`.get(columnIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SqlCell`](-sql-cell/index.md)<br>`operator fun `[`SqlRow`](-sql-row/index.md)`.get(columnName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlCell`](-sql-cell/index.md) |
| [select](select.md) | `suspend fun `[`SqlClient`](-sql-client/index.md)`.select(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlTables`](-sql-tables/index.md) |
