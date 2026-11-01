package com.jpapplications.carinsurance.domain.model

import kotlinx.serialization.json.JsonObject

data class CarRecommendationRequest(
    val rabattRelevanteDaten: JsonObject = JsonObject(emptyMap()),
    val versicherungsBeginn: String,
    val zahlungsintervall: String,
    val halterTyp: String,
    val fahrzeug: RecommendationVehicle,
    val fahrzeugLenker: RecommendationDriver,
    val abTestDtos: List<JsonObject>? = null,
)

data class RecommendationVehicle(
    val car: Car,
    val matriculationDate: Int,
    val newValueAccessories: Int,
    val financing: String,
    val vehicleUsage: String,
)

data class RecommendationDriver(
    val geschlecht: String,
    val geburtsdatum: String,
    val nationalitaet: String,
    val stadt: JsonObject,
    val jahrFahrpruefungPkw: Int,
    val schadensfaelle: List<JsonObject> = emptyList(),
)

data class RecommendationCity(
    val name: String,
    val data: JsonObject,
)

sealed interface CityLookupResult {
    data class Success(val city: RecommendationCity) : CityLookupResult

    data class Failure(val error: NetworkError) : CityLookupResult
}

data class CarRecommendation(
    val gesamtBruttoPraemie: Double,
    val metaData: JsonObject,
    val krf: String,
    val abTestDtos: List<JsonObject>? = null,
    val haftpflicht: JsonObject? = null,
    val kasko: JsonObject? = null,
    val insassenUnfallDto: JsonObject? = null,
    val assistance: Boolean? = null,
)

sealed interface CarRecommendationResult {
    data class Success(val recommendation: CarRecommendation) : CarRecommendationResult

    data class Failure(val error: NetworkError) : CarRecommendationResult
}
