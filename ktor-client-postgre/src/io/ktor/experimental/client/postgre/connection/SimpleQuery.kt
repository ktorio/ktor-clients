package io.ktor.experimental.client.postgre.connection

import io.ktor.experimental.client.postgre.*
import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.postgre.scheme.*
import io.ktor.experimental.client.sql.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.io.*

internal fun CoroutineScope.trySimplePipeline(
    firstPacket: PostgrePacket,
    input: ByteReadChannel
): QueryResult? {
    if (firstPacket.type != BackendMessage.ROW_DESCRIPTION && firstPacket.type != BackendMessage.COMMAND_COMPLETE) {
        return null
    }

    val tables = Channel<QueryResultTable>()

    launch {
        var packet = firstPacket
        lateinit var current: QueryResultTable
        var rows: Channel<PostgreRow>? = null

        while (true) {
            val payload = packet.payload

            when (packet.type) {
                BackendMessage.ROW_DESCRIPTION -> {
                    rows?.close()
                    rows = Channel()
                    current = PostgreResultTable(payload.readColumns(), rows, coroutineContext)
                    tables.send(current)
                }
                BackendMessage.DATA_ROW -> {
                    val data = payload.readRow()
                    rows!!.send(PostgreRow(current, data, coroutineContext))
                }
                BackendMessage.COMMAND_COMPLETE -> {
                    /* info */ payload.readCString()
                    rows?.close()
                }
                BackendMessage.READY_FOR_QUERY -> {
                    check(payload.remaining == 1L)
                    /* ignored in negotiate transaction status indicator */
                    payload.readByte()
                    return@launch
                }
                else -> checkErrors(packet.type, payload)
            }
            check(payload.remaining == 0L)

            packet = input.readPostgrePacket()
        }
    }.invokeOnCompletion {
        tables.close(it)
    }

    return tables
}
