package io.ktor.experimental.client.util

import kotlinx.coroutines.*

/**
 * Use [block] to complete [deferred], also handles [block] exceptions
 */
inline fun <T> CompletableDeferred<T>.completeWith(block: () -> T) {
    try {
        complete(block())
    } catch (cause: Throwable) {
        completeExceptionally(cause)
    }
}

suspend inline fun <T> deferred(block: (CompletableDeferred<T>) -> Unit): T {
    val deferred = CompletableDeferred<T>()
    block(deferred)
    return deferred.await()
}