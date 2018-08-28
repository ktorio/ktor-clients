package io.ktor.experimental.client.redis.geo

enum class GeoUnit(val symbol: String, val nmet: Double) {
    METERS("m", 1.0),
    KILOMETERS("km", 0.001),
    MILES("mi", 0.000621371),
    FEETS("ft", 3.28084);

    fun convertTo(value: Double, other: GeoUnit) = value * (other.nmet / this.nmet)
}