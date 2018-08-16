package io.ktor.experimental.client.redis

import org.junit.Test
import kotlin.test.*

class GeoTest {
    @Test
    fun testUnitConversion() {
        geoTools {
            assertEquals("10.0 km", 10.km.toString())
            assertEquals(true, 10.km == 10.km)
            assertEquals(10_000.0, 10.km.to(GeoUnit.METERS))
            assertEquals(2.0, 2000.m.to(GeoUnit.KILOMETERS))
        }
    }
}