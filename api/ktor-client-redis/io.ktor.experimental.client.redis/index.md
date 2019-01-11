[ktor-client-redis](../index.md) / [io.ktor.experimental.client.redis](./index.md)

## Package io.ktor.experimental.client.redis

### Types

| Name | Summary |
|---|---|
| [Redis](-redis/index.md) | `interface Redis : `[`Closeable`](http://docs.oracle.com/javase/6/docs/api/java/io/Closeable.html)<br>A Redis basic interface exposing emiting commands receiving their responses. |
| [RedisClient](-redis-client/index.md) | `class RedisClient : `[`Redis`](-redis/index.md)<br>Constructs a Redis client that will connect to [address](#) keeping a connection pool, keeping as much as [maxConnections](#) and using the [charset](-redis-client/charset.md). Optionally you can define the [password](#) of the connection. |
| [RedisClientReplyMode](-redis-client-reply-mode/index.md) | `enum class ~~RedisClientReplyMode~~` |
| [RedisInternalChannel](-redis-internal-channel.md) | `object ~~RedisInternalChannel~~` |
