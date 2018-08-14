package io.ktor.experimental.client.redis

data class GeoPosition(val longitude: Double, val latitude: Double)

enum class GeoUnit(val symbol: String) {
    METERS("m"),
    KILOMETERS("km"),
    MILES("mi"),
    FEET("ft")
}

/**
 * Add one or more geospatial items in the geospatial index represented using a sorted set
 *
 * https://redis.io/commands/geoadd
 *
 * @since 3.2.0
 */
suspend fun Redis.geoadd(key: String, vararg items: Pair<String, GeoPosition>) = commandLong(
    "geoadd", key, *(items.flatMap { listOf(it.second.longitude, it.second.latitude, it.first) }.toTypedArray())
)

/**
 * Returns the distance between two members of a geospatial index
 *
 * https://redis.io/commands/geodist
 *
 * @since 3.2.0
 */
suspend fun Redis.geodist(key: String, member1: String, member2: String, unit: GeoUnit = GeoUnit.METERS): Double? = commandDoubleOrNull(
    "geodist", key, member1, member2, unit.symbol
)

/**
 * Returns members of a geospatial index as standard geohash strings
 *
 * https://redis.io/commands/geohash
 *
 * @since 3.2.0
 */
suspend fun Redis.geohash(key: String, vararg members: String): List<String> = commandArrayString(
    "geohash", key, *members
)

/**
 * Returns longitude and latitude of members of a geospatial index
 *
 * https://redis.io/commands/geopos
 *
 * @since 3.2.0
 */
suspend fun Redis.geopos(key: String, vararg members: String): List<GeoPosition?> =
    commandArrayAny("geopos", key, *members).map {
        if (it != null && it is List<*>) {
            GeoPosition(it[0].toString().toDouble(), it[1].toString().toDouble())
        } else {
            null
        }
    }

/**
 * Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a point
 *
 * https://redis.io/commands/georadius
 *
 * @since 3.2.0
 */
internal suspend fun Redis.georadius(todo: Any): Any = TODO()

/**
 * Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a member
 *
 * https://redis.io/commands/georadiusbymember
 *
 * @since 3.2.0
 */
internal suspend fun Redis.georadiusbymember(todo: Any): Any = TODO()
