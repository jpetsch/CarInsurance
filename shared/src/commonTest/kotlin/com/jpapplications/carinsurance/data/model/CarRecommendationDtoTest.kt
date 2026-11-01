package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                        stadt =
                            RecommendationCityDto(
                                plz = 8000,
                                name = "Zürich",
                                kanton = "ZH",
                            ),
                        jahrFahrpruefungPkw = 2008,
                        schadensfaelle =
                            listOf(
                                RecommendationClaimDto(
                                    schadensfallTyp = "LIABILITY_MATERIAL",
                                    schadenfallYear = 2025,
                                    schadensfallSchuld = "YES_INSURANCE_PAYMENT",
                                    summe = 1200.0,
                                ),
                            ),
                    ),
            )

        val serialized = Json.encodeToString(request)

        assertTrue("\"rabattRelevanteDaten\":{}" in serialized)
        assertTrue("\"plz\":8000" in serialized)
        assertTrue("\"schadensfallTyp\":\"LIABILITY_MATERIAL\"" in serialized)
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
              "metaData": {
                "uwfLiability": "uwf-liability",
                "cffLiability": "cff-liability",
                "uwfAssistance": "uwf-assistance",
                "cffAssistance": "cff-assistance",
                "uwfHull": "uwf-hull",
                "cffHullTk": "cff-hull-tk",
                "cffHullVk": "cff-hull-vk",
                "uwfPassenger": "uwf-passenger",
                "cffPassenger": "cff-passenger",
                "keepOnChanges": false
              },
              "krf": "recommendation-token",
              "abTestDtos": [{"jiraTicketId": "SD-12685", "groupName": "B"}],
              "haftpflicht": {
                "bonusschutz": true,
                "gfverzicht": true,
                "bonusMalusStufe": 1,
                "bonusMalusProzent": 40,
                "haftpflichtSelbstbehalt": 0
              },
              "kasko": {
                "kaskoArt": "VOLLKASKO",
                "vollkaskoSelbstbehalt": 1000,
                "teilkaskoSelbstbehalt": 0,
                "entschaedigungsart": "PLUS",
                "parkschaeden": "INSURED_1000",
                "kaskoGlasbruch": "BASIC",
                "bonusschutz": true,
                "persoenlicheEffekten": "INSURED_2000",
                "bonusMalusStufe": 1,
                "bonusMalusProzent": 40
              },
              "insassenUnfallDto": {
                "insassenUnfallPersonenkreis": "ALL",
                "spitaltaggeld": 25,
                "taggeld": 25,
                "heilungskosten": true,
                "invaliditaetskapital": 60000,
                "todesfallkapital": 30000,
                "haustiere": "AMOUNT_3500"
              }
            }
            """.trimIndent()

        val recommendation = Json.decodeFromString<CarRecommendationDto>(json)

        assertEquals(1293.9, recommendation.gesamtBruttoPraemie)
        assertEquals("recommendation-token", recommendation.krf)
        assertEquals("uwf-liability", recommendation.metaData.uwfLiability)
        assertEquals("SD-12685", recommendation.abTestDtos?.first()?.jiraTicketId)
        assertEquals(40, recommendation.haftpflicht?.bonusMalusProzent)
        assertEquals("VOLLKASKO", recommendation.kasko?.kaskoArt)
        assertEquals(60000, recommendation.insassenUnfallDto?.invaliditaetskapital)
    }
}
