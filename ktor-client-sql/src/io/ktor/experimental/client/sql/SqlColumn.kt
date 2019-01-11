package io.ktor.experimental.client.sql

interface SqlColumn {
    val attributeID: Int
    val name: String
    val type: SqlType
}
