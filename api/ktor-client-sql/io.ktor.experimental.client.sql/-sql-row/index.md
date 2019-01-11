[ktor-client-sql](../../index.md) / [io.ktor.experimental.client.sql](../index.md) / [SqlRow](./index.md)

# SqlRow

`interface SqlRow : CoroutineScope`

### Properties

| Name | Summary |
|---|---|
| [columns](columns.md) | `open val columns: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SqlColumn`](../-sql-column/index.md)`>` |
| [result](result.md) | `abstract val result: `[`SqlTable`](../-sql-table/index.md) |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract operator fun get(column: `[`SqlColumn`](../-sql-column/index.md)`): `[`SqlCell`](../-sql-cell/index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [get](../get.md) | `operator fun `[`SqlRow`](./index.md)`.get(columnIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`SqlCell`](../-sql-cell/index.md)<br>`operator fun `[`SqlRow`](./index.md)`.get(columnName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SqlCell`](../-sql-cell/index.md) |
