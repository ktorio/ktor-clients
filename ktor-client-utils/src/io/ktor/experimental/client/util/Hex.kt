package io.ktor.experimental.client.util

private const val DIGITS = "0123456789ABCDEF"
private val DIGITS_LOWER = DIGITS.toLowerCase()

fun isHexDigit(c: Char) = c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'

private fun decodeHexDigit(c: Char): Int = when (c) {
    in '0'..'9' -> (c - '0') + 0
    in 'a'..'f' -> (c - 'a') + 10
    in 'A'..'F' -> (c - 'A') + 10
    else -> -1
}

private fun decode(value: String): ByteArray {
    val out = ByteArray(value.length / 2)
    var m = 0
    for (n in 0 until out.size) {
        val high = value[m++]
        val low = value[m++]
        val highDigit = decodeHexDigit(high)
        if (highDigit < 0)
            throw IllegalArgumentException("Cannot decode '$value' as a hex string. Character '$high' is not a hex digit.")
        val lowDigit = decodeHexDigit(low)
        if (lowDigit < 0)
            throw IllegalArgumentException("Cannot decode '$value' as a hex string. Character '$low' is not a hex digit.")
        out[n] = ((highDigit shl 4) or (lowDigit shl 0)).toByte()
    }
    return out
}

private fun encode(data: ByteArray, digits: String = DIGITS_LOWER): String = buildString(data.size * 2) {
    for (n in data.indices) {
        val v = data[n].toInt() and 0xFF
        append(digits[(v ushr 4) and 0xF])
        append(digits[(v ushr 0) and 0xF])
    }
}

fun ByteArray.toHexString(): String = encode(this)

fun String.fromHexString(): ByteArray = decode(this)
