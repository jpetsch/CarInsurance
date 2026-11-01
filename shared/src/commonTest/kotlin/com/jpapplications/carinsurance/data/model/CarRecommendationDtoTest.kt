package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CarRecommendationDtoTest {
    @Test
    fun requestSerializationMatchesInitialRecommendationPayload() {
        val request =
            CarRecommendationRequestDto(
                versicherungsBeginn = "2026-11-02",
                zahlungsintervall = "JAEHRLICH",
                halterTyp = "HAEUFIGSTERLENKER",
                fahrzeug =
                    RecommendationVehicleDto(
                        matriculationDate = 2020,
                        constructionStartYear = 2020,
                        catalogPrice = 50_000,
                        etxCode = 123,
                        description = "Vehicle",
                        capacity = 2000,
                        doors = 5,
                        seats = 5,
                        emptyLoad = 1500,
                        horsePower = 200,
                        enginePowerInKw = 150,
                        vehicleType = "CAR",
                        vehicleClass = 1,
                        constructionType = "SUV",
                        fuel = "PETROL",
                        brand = "Example",
                        model = "Model",
                        newValueAccessories = 3800,
                        financing = "SELF",
                        vehicleUsage = "PRIVAT_AND_TRAVEL",
                    ),
                fahrzeugLenker =
                    RecommendationDriverDto(
                        geschlecht = "MALE",
                        geburtsdatum = "1990-06-15",
                        nationalitaet = "CH",
                        stadt = buildJsonObject { put("name", "Zürich") },
                        jahrFahrpruefungPkw = 2008,
                    ),
            )

        val serialized = Json.encodeToString(request)

        assertTrue("\"rabattRelevanteDaten\":{}" in serialized)
        assertTrue("\"schadensfaelle\":[]" in serialized)
        assertTrue("\"fahrzeugLenker\"" in serialized)
        assertTrue("\"newValueAccessories\":3800" in serialized)
        assertFalse("\"abTestDtos\"" in serialized)
    }

    @Test
    fun recommendationResponseDeserializesRequiredAndOptionalFields() {
        val json =
            """
            {
              "gesamtBruttoPraemie": 1293.9,
              "metaData": {"calculationId": "test"},
              "krf": "recommendation-token",
              "abTestDtos": [{"name": "variant"}],
              "haftpflicht": {"premium": 500}
            }
            """.trimIndent()

        val recommendation = Json.decodeFromString<CarRecommendationDto>(json)

        assertEquals(1293.9, recommendation.gesamtBruttoPraemie)
        assertEquals("recommendation-token", recommendation.krf)
        assertEquals("test", recommendation.metaData["calculationId"]?.toString()?.trim('"'))
        assertEquals(1, recommendation.abTestDtos?.size)
        assertEquals(500, recommendation.haftpflicht?.get("premium")?.toString()?.toInt())
        assertEquals(null, recommendation.kasko)
    }
}
