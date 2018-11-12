package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*

data class PostgreColumn(
    override val name: String,
    val tableOID: Int,
    override val id: Int,
    val typeOID: Int,
    val typeSize: Int,
    val typeMod: Int,
    val text: Boolean
) : SqlColumn {
    override val type: SqlType = SqlType.fromPostgreTypeOID(typeOID)

    override fun toString(): String = "column:$name"
}

private fun SqlType.Companion.fromPostgreTypeOID(oid: Int): SqlType = SqlType.UNKNOWN
