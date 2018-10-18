package io.ktor.experimental.client.postgre.util

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