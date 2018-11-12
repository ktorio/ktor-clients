package io.ktor.experimental.client.util

import kotlinx.coroutines.*
import kotlin.coroutines.*


class ContextManager(
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val supervisor = SupervisorJob()

    fun createChild(): CoroutineContext = coroutineContext + CompletableDeferred<Unit>(supervisor)

    fun completeChild(coroutineContext: CoroutineContext): Unit {
        (coroutineContext[Job] as CompletableDeferred<Unit>).complete(Unit)
    }
}