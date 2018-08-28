package io.ktor.experimental.client.redis

import io.ktor.experimental.client.redis.geo.*
import org.junit.Test
import kotlin.test.*

class GeoTest {
    @Test
    fun testUnitConversion() {
        geoTools {
            assertEquals("10.0 km", 10.kilometers.toString())
            assertEquals(true, 10.kilometers == 10.kilometers)
            assertEquals(10_000.0, 10.kilometers.to(GeoUnit.METERS))
            assertEquals(2.0, 2000.meters.to(GeoUnit.KILOMETERS))
        }
    }
}