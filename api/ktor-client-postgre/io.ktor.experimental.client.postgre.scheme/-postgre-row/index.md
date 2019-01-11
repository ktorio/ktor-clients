[ktor-client-postgre](../../index.md) / [io.ktor.experimental.client.postgre.scheme](../index.md) / [PostgreRow](./index.md)

# PostgreRow

`class PostgreRow : SqlRow`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PostgreRow(result: SqlTable, data: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`?>, coroutineContext: <ERROR CLASS>)` |

### Properties

| Name | Summary |
|---|---|
| [coroutineContext](coroutine-context.md) | `val coroutineContext: <ERROR CLASS>` |
| [result](result.md) | `val result: SqlTable` |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun get(column: SqlColumn): SqlCell` |
