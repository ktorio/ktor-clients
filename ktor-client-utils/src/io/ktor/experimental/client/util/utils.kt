package io.ktor.experimental.client.util

import io.ktor.util.*

inline fun silent(block: () -> Unit) = try { block() } catch (_: Throwable) {}
