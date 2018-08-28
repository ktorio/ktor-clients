package io.ktor.experimental.client.redis.geo

data class GeoDistance(val value: Double, val unit: GeoUnit) {
    override fun toString(): String = "$value ${unit.symbol}"

    fun to(unit: GeoUnit) = this.unit.convertTo(value, unit)

    val meters get() = unit.convertTo(value, GeoUnit.METERS)
    val kilometers get() = unit.convertTo(value, GeoUnit.KILOMETERS)
    val miles get() = unit.convertTo(value, GeoUnit.MILES)
    val feets get() = unit.convertTo(value, GeoUnit.FEETS)

    companion object {
        val Number.meters get() = GeoDistance(
            this.toDouble(),
            GeoUnit.METERS
        )
        val Number.kilometers get() = GeoDistance(
            this.toDouble(),
            GeoUnit.KILOMETERS
        )
        val Number.miles get() = GeoDistance(
            this.toDouble(),
            GeoUnit.MILES
        )
        val Number.feets get() = GeoDistance(
            this.toDouble(),
            GeoUnit.FEETS
        )
    }
}