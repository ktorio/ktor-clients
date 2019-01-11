[ktor-client-postgre](../../index.md) / [io.ktor.experimental.client.postgre.scheme](../index.md) / [PostgreTable](./index.md)

# PostgreTable

`class PostgreTable : SqlTable, ReceiveChannel<SqlRow>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PostgreTable(columns: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<SqlColumn>, rows: Channel<`[`PostgreRow`](../-postgre-row/index.md)`>, coroutineContext: <ERROR CLASS>)` |

### Properties

| Name | Summary |
|---|---|
| [columns](columns.md) | `val columns: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<SqlColumn>` |
| [coroutineContext](coroutine-context.md) | `val coroutineContext: <ERROR CLASS>` |

### Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
