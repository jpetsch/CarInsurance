package com.jpapplications.carinsurance.data.repository

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RecommendationCityMapperTest {
    @Test
    fun mapsCityNameAndPreservesCityData() {
        val cityData =
            buildJsonObject {
                put("ort", "Zürich")
                put("zipcode", "8000")
            }

        val city = cityData.toRecommendationCity()

        assertEquals("Zürich", city?.name)
        assertEquals(cityData, city?.data)
    }

    @Test
    fun returnsNullWhenCityNameIsMissing() {
        val cityData = buildJsonObject { put("zipcode", "8000") }

        assertNull(cityData.toRecommendationCity())
    }
}
