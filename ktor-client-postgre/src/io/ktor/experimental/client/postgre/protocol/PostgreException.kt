package io.ktor.experimental.client.postgre.protocol

/**
 * TODO: remove map
 */
class PostgreException(
    val items: List<Pair<Char, String>>
) : RuntimeException() {
    private val parts: Map<Char, String> by lazy {
        items.toMap()
    }

    /**
     * Severity: the field contents are:
     * ERROR, FATAL, or PANIC (in an error message),
     * or WARNING, NOTICE, DEBUG, INFO,
     * or LOG (in a notice message),
     * or a localized translation of one of these.
     * Always present.
     */
    val severity: String get() = parts['S']!!

    /**
     * Code: the SQLSTATE code for the error (see Appendix A). Not localizable. Always present.
     */
    val sqlstate: String? get() = parts['C']

    /**
     * Message: the primary human-readable error message. This should be accurate but terse (typically one line). Always present.
     */
    val pmessage: String? get() = parts['M']

    // Detail: an optional secondary error message carrying more detail about the problem. Might run to multiple lines.
    val detail: String? get() = parts['D']

    // Hint: an optional suggestion what to do about the problem. This is intended to differ from Detail in that it offers advice (potentially inappropriate) rather than hard facts. Might run to multiple lines.
    val hint: String? get() = parts['H']

    // Position: the field value is a decimal ASCII integer, indicating an error cursor position as an index into the original query string. The first character has index 1, and positions are measured in characters not bytes.
    val position: String? get() = parts['P']

    // Internal position: this is defined the same as the P field, but it is used when the cursor position refers to an internally generated command rather than the one submitted by the client. The q field will always appear when this field appears.
    val internalPosition: String? get() = parts['p']

    // Internal query: the text of a failed internally-generated command. This could be, for example, a SQL query issued by a PL/pgSQL function.
    val internalQuery: String? get() = parts['q']

    // Where: an indication of the context in which the error occurred. Presently this includes a call stack traceback of active procedural language functions and internally-generated queries. The trace is one entry per line, most recent first.
    val where: String? get() = parts['W']

    // Schema name: if the error was associated with a specific database object, the name of the schema containing that object, if any.
    val schemaName: String? get() = parts['s']

    // Table name: if the error was associated with a specific table, the name of the table. (Refer to the schema name field for the name of the table's schema.)
    val tableName: String? get() = parts['t']

    // Column name: if the error was associated with a specific table column, the name of the column. (Refer to the schema and table name fields to identify the table.)
    val columnName: String? get() = parts['c']

    // Data type name: if the error was associated with a specific data type, the name of the data type. (Refer to the schema name field for the name of the data type's schema.)
    val dataTypeName: String? get() = parts['d']

    // Constraint name: if the error was associated with a specific constraint, the name of the constraint. Refer to fields listed above for the associated table or domain. (For this purpose, indexes are treated as constraints, even if they weren't created with constraint syntax.)
    val contraintName: String? get() = parts['n']

    // File: the file name of the source-code location where the error was reported.
    val fileName: String? get() = parts['F']

    // Line: the line number of the source-code location where the error was reported.
    val line: String? get() = parts['L']

    // Routine: the name of the source-code routine reporting the error.
    val routine: String? get() = parts['R']

    override val message: String? = "$severity: $pmessage ($parts)"
}