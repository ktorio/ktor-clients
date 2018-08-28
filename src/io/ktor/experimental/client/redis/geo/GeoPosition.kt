package io.ktor.experimental.client.redis.geo

data class GeoPosition(val longitude: Double, val latitude: Double) {
    companion object {
        @Suppress("NOTHING_TO_INLINE")
        inline operator fun invoke(longitude: Number, latitude: Number): GeoPosition =
            GeoPosition(longitude.toDouble(), latitude.toDouble())
    }
}