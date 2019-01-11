[ktor-client-redis](../../index.md) / [io.ktor.experimental.client.redis](../index.md) / [RedisClient](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RedisClient(host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, port: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = Redis.DEFAULT_PORT, maxConnections: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 50, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, charset: `[`Charset`](http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html)` = Charsets.UTF_8, dispatcher: CoroutineDispatcher = Dispatchers.Default)``RedisClient(address: `[`SocketAddress`](http://docs.oracle.com/javase/6/docs/api/java/net/SocketAddress.html)` = InetSocketAddress("127.0.0.1", 6379), maxConnections: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 50, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, charset: `[`Charset`](http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html)` = Charsets.UTF_8, dispatcher: CoroutineDispatcher = Dispatchers.Default)`

Constructs a Redis client that will connect to [address](#) keeping a connection pool,
keeping as much as [maxConnections](#) and using the [charset](charset.md).
Optionally you can define the [password](#) of the connection.

