package io.ktor.experimental.client.redis

import io.ktor.experimental.client.redis.protocol.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import java.io.*
import java.net.*
import java.nio.charset.*
import java.util.concurrent.atomic.*

/**
 * A Redis basic interface exposing emiting commands receiving their responses.
 *
 * Specific commands are exposed as extension methods.
 */
interface Redis : Closeable {
    companion object {
        val DEFAULT_PORT = 6379
    }

    /**
     * Use [context] to await client close or terminate
     */
    val context: Job

    /**
     * Chatset that
     */
    val charset: Charset get() = Charsets.UTF_8

    /**
     * Executes a raw command. Each [args] will be sent as a String.
     *
     * It returns a type depending on the command.
     * The returned value can be of type [String], [Long] or [List].
     *
     * It may throw a [RedisResponseException]
     */
    suspend fun execute(vararg args: Any?): Any?

    fun RedisInternalChannel.setReplyMode(mode: RedisClientReplyMode) = Unit

    fun RedisInternalChannel.getMessageChannel(): ReceiveChannel<Any> = Channel<Any>(0).apply { close() }
}

@Deprecated("Do not use for now")
object RedisInternalChannel

@Deprecated("Do not use for now")
enum class RedisClientReplyMode { ON, OFF, SKIP }

class RedisMultiClient(
    private val addresses: List<SocketAddress> = listOf(InetSocketAddress("127.0.0.1", 6379)),
    private val maxConnections: Int = 50,
    private val password: String? = null,
    val charset: Charset = Charsets.UTF_8,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcher
) {
    private val clients = Channel<Redis>(maxConnections)
    private val allocatedClients = AtomicInteger()
    private var addressOffset = 0

    /**
     * Gets a temporal Redis instance to perform operations on a single redis instance.
     */
    suspend fun session(callback: suspend Redis.() -> Unit) {
        val client = getOrAllocateClient()
        try {
            callback(client)
        } finally {
            releaseClient(client)
        }
    }

    private suspend fun getOrAllocateClient(): Redis {
        return if (allocatedClients.get() < maxConnections) {
            clients.poll() ?: allocateClient()
        } else {
            clients.receive()
        }
    }

    private fun allocateClient(): Redis {
        allocatedClients.incrementAndGet()
        return RedisClient(addresses[addressOffset++ % addresses.size], password, charset, dispatcher)
    }

    private fun releaseClient(client: Redis): Unit {
        clients.offer(client)
    }

}

/**
 * TODO
 * 1. add pipeline timeouts
 * 2. multiple endpoints (since the point of having several connections is mostly multiple endpoints)
 * 3. redis connections are stateful, so the connection pool cannot be done at this level
 */

/**
 * Constructs a Redis client that will connect to [address] keeping a connection pool,
 * keeping as much as [maxConnections] and using the [charset].
 * Optionally you can define the [password] of the connection.
 */
class RedisClient(
    private val address: SocketAddress = InetSocketAddress("127.0.0.1", 6379),
    private val password: String? = null,
    override val charset: Charset = Charsets.UTF_8,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcher
) : Redis {
    constructor(
        host: String,
        port: Int = Redis.DEFAULT_PORT,
        password: String? = null,
        charset: Charset = Charsets.UTF_8,
        dispatcher: CoroutineDispatcher = DefaultDispatcher
    ) : this(
        InetSocketAddress(host, port),
        password,
        charset,
        dispatcher
    )

    override val context: Job = Job()

    private val selectorManager = ActorSelectorManager(dispatcher)
    private val requestQueue = Channel<RedisRequest>()
    private lateinit var pipeline: ConnectionPipeline

    private val postmanService = actor<RedisRequest>(dispatcher, parent = context) {
        channel.consumeEach {
            if (!::pipeline.isInitialized) {
                val socket = aSocket(selectorManager)
                    .tcpNoDelay()
                    .tcp()
                    .connect(address)
                pipeline = ConnectionPipeline(socket, requestQueue, password, charset, dispatcher = dispatcher)
            }

            requestQueue.send(it)
        }

        requestQueue.close()
    }

    init {
        context.invokeOnCompletion {
            selectorManager.close()
        }
    }

    override suspend fun execute(vararg args: Any?): Any? {
        return when (rmode) {
            RedisClientReplyMode.ON, RedisClientReplyMode.SKIP -> {
                val result = CompletableDeferred<Any?>()
                postmanService.send(RedisRequest(args, result))
                if (rmode != RedisClientReplyMode.SKIP) {
                    try {
                        result.await()
                    } catch (e: RedisException) {
                        throw RedisException(e.message ?: "error", args)
                    }
                } else {
                    null
                }
            }
            else -> {
                postmanService.send(RedisRequest(args, null))
                null
            }
        }
    }

    override fun close() {
        context.cancel()
    }

    private var rmode = RedisClientReplyMode.ON

    override fun RedisInternalChannel.setReplyMode(mode: RedisClientReplyMode) {
        rmode = mode
    }

    override fun RedisInternalChannel.getMessageChannel(): ReceiveChannel<Any> {
        setReplyMode(RedisClientReplyMode.OFF)
        return produce(context) {
            while (true) {
                val result = CompletableDeferred<Any?>()
                postmanService.send(RedisRequest(null, result))
                send(result.await()?.byteArraysToString ?: Unit)
            }
        }
    }
}
