package io.ktor.experimental.client.redis

import io.ktor.experimental.client.redis.geo.*

/**
 * Add one or more geospatial items in the geospatial index represented using a sorted set
 *
 * https://redis.io/commands/geoadd
 *
 * @since 3.2.0
 */
suspend fun Redis.geoadd(key: String, vararg items: Pair<String, GeoPosition>) = executeTyped<Long>(
    "GEOADD", key, *(items.flatMap { listOf(it.second.longitude, it.second.latitude, it.first) }.toTypedArray())
)

/**
 * Returns the distance between two members of a geospatial index
 *
 * https://redis.io/commands/geodist
 *
 * @since 3.2.0
 */
suspend fun Redis.geodist(key: String, member1: String, member2: String, unit: GeoUnit = GeoUnit.METERS): Double? =
    executeTypedNull<Double>("GEODIST", key, member1, member2, unit.symbol)

/**
 * Returns members of a geospatial index as standard geohash strings
 *
 * https://redis.io/commands/geohash
 *
 * @since 3.2.0
 */
suspend fun Redis.geohash(key: String, vararg members: String): List<String> =
    executeArrayString("GEOHASH", key, *members)

/**
 * Returns longitude and latitude of members of a geospatial index
 *
 * https://redis.io/commands/geopos
 *
 * @since 3.2.0
 */
suspend fun Redis.geopos(key: String, vararg members: String): List<GeoPosition?> =
    executeArrayAny("GEOPOS", key, *members).map {
        if (it != null && it is List<*> && it.size >= 2) {
            GeoPosition(it[0].toString().toDouble(), it[1].toString().toDouble())
        } else {
            null
        }
    }

data class GeoRadiusResult(
    val name: String,
    val distance: GeoDistance? = null,
    val coords: GeoPosition? = null,
    val hash: Long? = null
)

/**
 * Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a point
 *
 * https://redis.io/commands/georadius
 *
 * @since 3.2.0
 */
suspend fun Redis.georadius(
    key: String,
    pos: GeoPosition,
    radius: GeoDistance,
    withCoord: Boolean = false,
    withDist: Boolean = false,
    withHash: Boolean = false,
    count: Long? = null,
    sort: SortDirection? = null,
    storeKey: String? = null,
    storeDistKey: String? = null
): List<GeoRadiusResult> = geoRadiusCommon(
    key, pos, radius, withCoord, withDist, withHash, count, sort, storeKey, storeDistKey
)

/**
 * Query a sorted set representing a geospatial index to fetch members matching a given maximum distance from a member
 *
 * https://redis.io/commands/georadiusbymember
 *
 * @since 3.2.0
 */
suspend fun Redis.georadiusbymember(
    key: String,
    member: String,
    radius: GeoDistance,
    withCoord: Boolean = false,
    withDist: Boolean = false,
    withHash: Boolean = false,
    count: Long? = null,
    sort: SortDirection? = null,
    storeKey: String? = null,
    storeDistKey: String? = null
): List<GeoRadiusResult> = geoRadiusCommon(
    key, member, radius, withCoord, withDist, withHash, count, sort, storeKey, storeDistKey
)

private suspend fun Redis.geoRadiusCommon(
    key: String,
    reference: Any?,
    radius: GeoDistance,
    withCoord: Boolean = false,
    withDist: Boolean = false,
    withHash: Boolean = false,
    count: Long? = null,
    sort: SortDirection? = null,
    storeKey: String? = null,
    storeDistKey: String? = null
): List<GeoRadiusResult>{
    val radiusValue = radius.value
    val radiusUnit = radius.unit
    val cmds = arrayListOf<Any?>()
    cmds += if (reference is GeoPosition) "GEORADIUS" else "GEORADIUSBYMEMBER"
    cmds += key
    if (reference is GeoPosition) {
        cmds += reference.longitude
        cmds += reference.latitude
    } else {
        cmds += reference.toString()
    }
    cmds += radiusValue
    cmds += radiusUnit.symbol
    if (withCoord) cmds += "WITHCOORD"
    if (withDist) cmds += "WITHDIST"
    if (withHash) cmds += "WITHHASH"
    if (count != null) {
        cmds += "COUNT"
        cmds += count
    }
    if (sort != null) {
        cmds += sort.name
    }
    if (storeKey != null) {
        cmds += "STORE"
        cmds += storeKey
    }
    if (storeDistKey != null) {
        cmds += "STOREDIST"
        cmds += storeDistKey
    }
    val result = executeText(*cmds.toTypedArray())
    if (result is List<*>) {
        @Suppress("UNUSED_CHANGED_VALUE")
        return result.map {
            val item = it as List<*>
            var pos = 0
            val name = item[pos++].toString()
            val dist = if (withDist) item[pos++]?.toString()?.toDoubleOrNull() else null
            val hash = if (withHash) item[pos++].toString()?.toLongOrNull() else null
            val coords = if (withCoord) (item[pos++] as List<*>).map { it.toString().toDouble() } else null
            GeoRadiusResult(
                name, dist?.let { GeoDistance(it, radiusUnit) }, coords?.let {
                    GeoPosition(
                        it[0],
                        it[1]
                    )
                }, hash
            )
        }
    }
    return listOf()
}