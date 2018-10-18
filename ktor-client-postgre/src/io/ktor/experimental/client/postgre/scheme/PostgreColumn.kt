package io.ktor.experimental.client.postgre.scheme

data class PostgreColumn(
    val name: String,
    val tableOID: Int,
    val columnIndex: Int,
    val typeOID: Int,
    val typeSize: Int,
    val typeMod: Int,
    val text: Boolean
)
