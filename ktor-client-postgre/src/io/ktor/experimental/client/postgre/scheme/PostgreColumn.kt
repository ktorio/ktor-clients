package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*

data class PostgreColumn(
    /**
     * The field name.
     */
    override val name: String,

    /**
     * If the field can be identified as a column of a specific table, the object ID of the table; otherwise zero.
     */
    val tableOID: Int,

    /**
     * If the field can be identified as a column of a specific table, the attribute number of the column; otherwise zero.
     */
    override val attributeID: Int,

    /**
     * The object ID of the field's data type.
     */
    val typeOID: Int,

    /**
     * The data type size (see pg_type.typlen). Note that negative values denote variable-width types.
     */
    val typeSize: Int,

    /**
     * The type modifier (see pg_attribute.atttypmod). The meaning of the modifier is type-specific.
     */
    val typeMod: Int,

    /**
     * Is set to `true` if the field is encoded as text. 
     */
    val text: Boolean
) : SqlColumn {
    override val type: SqlType = SqlType.fromPostgreTypeOID(typeOID)

    override fun toString(): String = "column:$name"
}

private fun SqlType.Companion.fromPostgreTypeOID(oid: Int): SqlType = SqlType.UNKNOWN
