[ktor-client-redis](../../index.md) / [io.ktor.experimental.client.redis.commands](../index.md) / [RedisBitFieldBuilder](./index.md)

# RedisBitFieldBuilder

`class RedisBitFieldBuilder`

### Types

| Name | Summary |
|---|---|
| [Type](-type/index.md) | `data class Type` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RedisBitFieldBuilder()` |

### Properties

| Name | Summary |
|---|---|
| [cmds](cmds.md) | `val cmds: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>` |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun get(type: `[`Type`](-type/index.md)`, offset: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [i](i.md) | `fun i(bits: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Type`](-type/index.md) |
| [incrby](incrby.md) | `fun incrby(type: `[`Type`](-type/index.md)`, offset: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, increment: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [overflowFail](overflow-fail.md) | `fun overflowFail(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [overflowSaturate](overflow-saturate.md) | `fun overflowSaturate(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [overflowWrap](overflow-wrap.md) | `fun overflowWrap(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [set](set.md) | `fun set(type: `[`Type`](-type/index.md)`, offset: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, value: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [type](type.md) | `fun type(bits: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, signed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Type`](-type/index.md) |
| [u](u.md) | `fun u(bits: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Type`](-type/index.md) |
