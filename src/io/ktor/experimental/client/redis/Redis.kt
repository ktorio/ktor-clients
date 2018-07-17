package io.ktor.experimental.client.redis

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
}

/**
 * TODO
 * 1. add pipeline timeouts
 */

/**
 * Constructs a Redis client that will connect to [address] keeping a connection pool,
 * keeping as much as [maxConnections] and using the [charset].
 * Optionally you can define the [password] of the connection.
 */
class RedisClient(
    private val address: SocketAddress = InetSocketAddress("127.0.0.1", 6379),
    maxConnections: Int = 50,
    private val password: String? = null,
    override val charset: Charset = Charsets.UTF_8,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcher
) : Redis {
    override val context: Job = Job()

    private val runningPipelines = AtomicInteger()
    private val selectorManager = ActorSelectorManager(dispatcher)
    private val requestQueue = Channel<RedisRequest>()

    private val postmanService = actor<RedisRequest>(
        dispatcher, parent = context
    ) {
        channel.consumeEach {
            if (requestQueue.offer(it)) return@consumeEach

            while (true) {
                val current = runningPipelines.get()
                if (current >= maxConnections) break

                if (!runningPipelines.compareAndSet(current, current + 1)) continue

                createNewPipeline()
                break
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
        val result = CompletableDeferred<Any?>()
        postmanService.send(RedisRequest(args, result))
        return result.await()
    }

    override fun close() {
        context.cancel()
    }

    private suspend fun createNewPipeline() {
        val socket = aSocket(selectorManager)
            .tcpNoDelay()
            .tcp()
            .connect(address)

        val pipeline = ConnectionPipeline(socket, requestQueue, password, charset, dispatcher = dispatcher)

        pipeline.context.invokeOnCompletion {
            runningPipelines.decrementAndGet()
        }
    }
}
