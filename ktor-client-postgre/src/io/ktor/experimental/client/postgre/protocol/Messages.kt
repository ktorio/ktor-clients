package io.ktor.experimental.client.postgre.protocol

/**
 * According to:
 * https://www.postgresql.org/docs/current/static/protocol-message-formats.html
 *
 * note that length is always includes itself
 */

internal enum class BackendMessage(val code: Char) {
    EMPTY(0.toChar()),

    AUTHENTICATION_REQUEST('R'),

    BACKEND_KEY_DATA('K'),

    PARSE_COMPLETE('1'),
    BIND_COMPLETE('2'),
    CLOSE_COMPLETE('3'),

    COMMAND_COMPLETE('C'),

    COPY_DATA('d'),
    COPY_DONE('c'),

    COPY_IN_RESPONSE('G'),
    COPY_OUT_RESPONSE('H'),
    COPY_BOTH_RESPONSE('W'),

    DATA_ROW('D'),
    EMPTY_QUERY_RESPONSE('I'),

    ERROR_RESPONSE('E'),

    FUNCTION_CALL_RESPONSE('V'),

    NEGOTIATE_PROTOCOL_VERSION('v'),
    NO_DATA('n'),
    NOTICE_RESPONSE('N'),
    NOTIFICATION_RESPONSE('A'),

    PARAMETER_DESCRIPTION('t'),
    PARAMETER_STATUS('S'),

    PORTAL_SUSPENDED('s'),
    READY_FOR_QUERY('Z'),
    ROW_DESCRIPTION('T');

    companion object {
        private val allValues = Array(256) { index ->
            BackendMessage.values().find { it.code == index.toChar() } ?: EMPTY
        }

        fun fromValue(value: Byte): BackendMessage = allValues[value.toInt() and 0xff]
    }
}

internal enum class FrontendMessage(val code: Char) {
    CANCEL_REQUEST(0.toChar()),
    SSL_REQUEST(0.toChar()),
    STARTUP_MESSAGE(0.toChar()),

    BIND('B'),
    CLOSE('C'),

    COPY_DATA('d'),
    COPY_DONE('c'),
    COPY_FAIL('f'),

    DESCRIBE('D'),
    EXECUTE('E'),
    FLUSH('H'),
    FUNCTION_CALL('F'),
    PARSE('P'),
    QUERY('Q'),
    SYNC('S'),
    TERMINATE('X'),

    GSS_RESPONSE('p'),
    PASSWORD_MESSAGE('p'),
    SASL_INITIAL_RESPONSE('p'),
    SASL_RESPONSE('p');
}

internal enum class AuthenticationType(val code: Int) {
    OK(0),
    KERBEROS_V5(2),
    CLEARTEXT_PASSWORD(3),
    MD5_PASSWORD(5),
    SCM_CREDENTIAL(6),
    GSS(7),
    SSPI(9),

    /**
     * note that length of the following messages is before the [code]
     */
    GSS_CONTINUE(8),
    SASL(10),

    SASL_CONTINUE(11),
    SASL_FINAL(12);

    companion object {
        fun fromCode(code: Int): AuthenticationType = values().find { code == it.code }!!
    }
}

