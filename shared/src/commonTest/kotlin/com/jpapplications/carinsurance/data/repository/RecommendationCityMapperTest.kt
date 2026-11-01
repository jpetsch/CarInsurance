package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.RecommendationCityDto
import com.jpapplications.carinsurance.domain.model.RecommendationCity
import kotlin.test.Test
import kotlin.test.assertEquals

class RecommendationCityMapperTest {
    @Test
    fun mapsTypedCityResponseToDomainCity() {
        val city =
            RecommendationCityDto(
                plz = 8000,
                name = "Zürich",
                kanton = "ZH",
            ).toDomain()

        assertEquals(
            RecommendationCity(
                plz = 8000,
                name = "Zürich",
                kanton = "ZH",
            ),
            city,
        )
    }
}
