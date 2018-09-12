package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.io.*
import kotlinx.io.core.*

internal fun buildChannel(block: BytePacketBuilder.() -> Unit): ByteReadChannel {
    val content = buildPacket(block = block).readBytes()
    return ByteReadChannel(content)
}
