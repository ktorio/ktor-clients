[ktor-client-postgre](../../index.md) / [io.ktor.experimental.client.postgre.scheme](../index.md) / [PostgreCell](./index.md)

# PostgreCell

`class PostgreCell : SqlCell`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PostgreCell(row: SqlRow, coroutineContext: <ERROR CLASS>, column: SqlColumn, content: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [column](column.md) | `val column: SqlColumn` |
| [content](content.md) | `val content: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`?` |
| [coroutineContext](coroutine-context.md) | `val coroutineContext: <ERROR CLASS>` |
| [row](row.md) | `val row: SqlRow` |

### Functions

| Name | Summary |
|---|---|
| [asArray](as-array.md) | `fun <T> asArray(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](as-array.md#T)`>` |
| [asBoolean](as-boolean.md) | `fun asBoolean(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [asByte](as-byte.md) | `fun asByte(): `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html) |
| [asBytes](as-bytes.md) | `fun asBytes(): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [asDouble](as-double.md) | `fun asDouble(): `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [asFloat](as-float.md) | `fun asFloat(): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [asInt](as-int.md) | `fun asInt(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [asPacket](as-packet.md) | `fun asPacket(): ByteReadPacket` |
| [asString](as-string.md) | `fun asString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [asType](as-type.md) | `fun <T> asType(): `[`T`](as-type.md#T) |
| [isNull](is-null.md) | `fun isNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
