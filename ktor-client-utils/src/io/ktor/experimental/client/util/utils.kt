package io.ktor.experimental.client.util

inline fun silent(block: () -> Unit) = try { block() } catch (_: Throwable) {}
