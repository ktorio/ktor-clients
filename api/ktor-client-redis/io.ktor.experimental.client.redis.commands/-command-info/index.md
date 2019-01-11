[ktor-client-redis](../../index.md) / [io.ktor.experimental.client.redis.commands](../index.md) / [CommandInfo](./index.md)

# CommandInfo

`data class CommandInfo`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CommandInfo(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, arity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, firstKey: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, lastKey: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, step: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [arity](arity.md) | `val arity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>command arity specification |
| [firstKey](first-key.md) | `val firstKey: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>position of first key in argument list |
| [flags](flags.md) | `val flags: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>nested Array reply of command flags |
| [hasAdmin](has-admin.md) | `val hasAdmin: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>server admin command |
| [hasAsking](has-asking.md) | `val hasAsking: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>cluster related - accept even if importing |
| [hasDenyoom](has-denyoom.md) | `val hasDenyoom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>reject command if currently OOM |
| [hasFast](has-fast.md) | `val hasFast: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>command operates in constant or log(N) time. Used for latency monitoring. |
| [hasLoading](has-loading.md) | `val hasLoading: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>allow command while database is loading |
| [hasMoveableKeys](has-moveable-keys.md) | `val hasMoveableKeys: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>keys have no pre-determined position. You must discover keys yourself. |
| [hasNoscript](has-noscript.md) | `val hasNoscript: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>deny this command from scripts |
| [hasPubsub](has-pubsub.md) | `val hasPubsub: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>pubsub-related command |
| [hasRandom](has-random.md) | `val hasRandom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>command has random results, dangerous for scripts |
| [hasReadonly](has-readonly.md) | `val hasReadonly: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>command will never modify keys |
| [hasSkipMonitor](has-skip-monitor.md) | `val hasSkipMonitor: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>do not show this command in MONITOR |
| [hasSortForScripts](has-sort-for-scripts.md) | `val hasSortForScripts: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>if called from script, sort output |
| [hasStale](has-stale.md) | `val hasStale: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>allow command while replica has stale data |
| [hasWrite](has-write.md) | `val hasWrite: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>command may result in modifications |
| [lastKey](last-key.md) | `val lastKey: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>position of last key in argument list |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>command name |
| [step](step.md) | `val step: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>step count for locating repeating keys |
