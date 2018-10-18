package io.ktor.experimental.client.postgre.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.io.core.*
import java.util.concurrent.atomic.*

class PipelineElement<TRequest : Any, TResponse : Any>(
    val request: TRequest,
    val response: CompletableDeferred<TResponse>
)

abstract class ConnectionPipeline<TRequest : Any, TResponse : Any>(
    source: ReceiveChannel<PipelineElement<TRequest, TResponse>>,
    pipelineSize: Int = 10
) : Closeable {
    private val started = AtomicBoolean(false)

    private val context: Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        try {
            onStart()
            for (element in source) {
                try {
                    reader.send(element)
                } catch (cause: Throwable) {
                    val origin = cause.withRequest(element.request)
                    element.response.completeExceptionally(origin)
                    throw cause
                }

                send(element.request)
            }
        } catch (cause: CancellationException) {
        } catch (cause: Throwable) {
            reader.close(cause)
            onError(cause)
        } finally {
            reader.close()
            try {
                onDone()
            } catch (_: Throwable) {
            }
        }
    }

    private val reader = GlobalScope.actor<PipelineElement<TRequest, TResponse>>(
        capacity = pipelineSize,
        context = Dispatchers.Default + context,
        start = CoroutineStart.LAZY
    ) {
        for (element in channel) {
            element.response.completeWith {
                try {
                    this@ConnectionPipeline.receive()
                } catch (cause: Throwable) {
                    throw cause.withRequest(element.request)
                }
            }
        }
    }

    protected abstract suspend fun send(request: TRequest)

    protected abstract suspend fun receive(): TResponse

    protected open suspend fun onStart() {}
    protected open fun onDone() {}
    protected open fun onError(cause: Throwable) {}

    override fun close() {
        context.cancel()
    }
}

class PipelineException(val request: Any, override val cause: Throwable) : RuntimeException() {
    override val message: String = "Fail to execute: $request"
}

private fun Throwable.withRequest(request: Any): Throwable =
    PipelineException(request, this)

