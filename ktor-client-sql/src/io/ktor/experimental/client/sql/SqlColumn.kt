package io.ktor.experimental.client.sql

interface SqlColumn {
    val id: Int
    val name: String
    val type: SqlType
}
