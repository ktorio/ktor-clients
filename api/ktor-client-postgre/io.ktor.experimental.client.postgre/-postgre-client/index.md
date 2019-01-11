[ktor-client-postgre](../../index.md) / [io.ktor.experimental.client.postgre](../index.md) / [PostgreClient](./index.md)

# PostgreClient

`class PostgreClient : SqlClient, Closeable`

https://www.postgresql.org/docs/11/static/index.html

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PostgreClient(address: `[`InetSocketAddress`](http://docs.oracle.com/javase/6/docs/api/java/net/InetSocketAddress.html)`, database: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "default", user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "root", password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, maxConnections: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1)`<br>https://www.postgresql.org/docs/11/static/index.html |

### Properties

| Name | Summary |
|---|---|
| [address](address.md) | `val address: `[`InetSocketAddress`](http://docs.oracle.com/javase/6/docs/api/java/net/InetSocketAddress.html) |
| [coroutineContext](coroutine-context.md) | `val coroutineContext: <ERROR CLASS>` |
| [database](database.md) | `val database: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [user](user.md) | `val user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [close](close.md) | `fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [connection](connection.md) | `suspend fun connection(): SqlConnection` |
| [execute](execute.md) | `suspend fun execute(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): SqlQueryResult` |
| [prepare](prepare.md) | `suspend fun prepare(queryString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): SqlStatement` |
