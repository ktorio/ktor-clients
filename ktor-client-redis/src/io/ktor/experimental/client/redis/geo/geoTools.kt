package io.ktor.experimental.client.redis.geo

inline fun geoTools(callback: GeoDistance.Companion.() -> Unit) {
    callback(GeoDistance)
}