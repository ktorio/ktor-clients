package io.ktor.experimental.client.postgre.util

object Hex {
    private const val DIGITS = "0123456789ABCDEF"
    private val DIGITS_LOWER = DIGITS.toLowerCase()

    fun isHexDigit(c: Char) = c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'

    fun decodeHexDigit(c: Char): Int = when (c) {
        in '0'..'9' -> (c - '0') + 0
        in 'a'..'f' -> (c - 'a') + 10
        in 'A'..'F' -> (c - 'A') + 10
        else -> throw IllegalArgumentException("Not an hex digit")
    }

    fun decode(value: String): ByteArray {
        val out = ByteArray(value.length / 2)
        var m = 0
        for (n in 0 until out.size) {
            val high = value[m++]
            val low = value[m++]
            out[n] = ((decodeHexDigit(high) shl 4) or (decodeHexDigit(
                low
            ) shl 0)).toByte()
        }
        return out
    }

    fun encode(src: ByteArray): String = encodeBase(
        src,
        DIGITS_LOWER
    )

    private fun encodeBase(data: ByteArray, digits: String): String = buildString(data.size * 2) {
        for (n in data.indices) {
            val v = data[n].toInt() and 0xFF
            append(digits[(v ushr 4) and 0xF])
            append(digits[(v ushr 0) and 0xF])
        }
    }
}

fun ByteArray.toHex(): String = Hex.encode(this)

fun String.fromHex(): ByteArray = Hex.decode(this)
