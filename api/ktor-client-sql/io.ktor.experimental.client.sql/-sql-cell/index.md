[ktor-client-sql](../../index.md) / [io.ktor.experimental.client.sql](../index.md) / [SqlCell](./index.md)

# SqlCell

`interface SqlCell : CoroutineScope`

### Properties

| Name | Summary |
|---|---|
| [column](column.md) | `abstract val column: `[`SqlColumn`](../-sql-column/index.md) |
| [row](row.md) | `abstract val row: `[`SqlRow`](../-sql-row/index.md) |

### Functions

| Name | Summary |
|---|---|
| [asArray](as-array.md) | `abstract fun <T> asArray(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](as-array.md#T)`>` |
| [asBoolean](as-boolean.md) | `abstract fun asBoolean(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [asByte](as-byte.md) | `abstract fun asByte(): `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html) |
| [asBytes](as-bytes.md) | `abstract fun asBytes(): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [asDouble](as-double.md) | `abstract fun asDouble(): `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [asFloat](as-float.md) | `abstract fun asFloat(): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [asInt](as-int.md) | `abstract fun asInt(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [asPacket](as-packet.md) | `abstract fun asPacket(): ByteReadPacket` |
| [asString](as-string.md) | `abstract fun asString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [asType](as-type.md) | `abstract fun <T> asType(): `[`T`](as-type.md#T) |
| [isNull](is-null.md) | `abstract fun isNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
