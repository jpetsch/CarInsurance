package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CarRecommendationRequestDto(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val rabattRelevanteDaten: JsonObject = JsonObject(emptyMap()),
    val versicherungsBeginn: String,
    val zahlungsintervall: String,
    val halterTyp: String,
    val fahrzeug: RecommendationVehicleDto,
    val fahrzeugLenker: RecommendationDriverDto,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val abTestDtos: List<JsonObject>? = null,
)

@Serializable
data class RecommendationVehicleDto(
    val matriculationDate: Int,
    val constructionStartYear: Int,
    val catalogPrice: Int,
    val etxCode: Int,
    val description: String,
    val capacity: Int,
    val doors: Int,
    val seats: Int,
    val emptyLoad: Int,
    val horsePower: Int,
    val enginePowerInKw: Int,
    val vehicleType: String,
    val vehicleClass: Int,
    val constructionType: String,
    val fuel: String,
    val brand: String,
    val model: String,
    val eMobility: Boolean = false,
    val newValueAccessories: Int,
    val financing: String,
    val vehicleUsage: String,
)

@Serializable
data class RecommendationDriverDto(
    val geschlecht: String,
    val geburtsdatum: String,
    val nationalitaet: String,
    val stadt: JsonObject,
    val jahrFahrpruefungPkw: Int,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val schadensfaelle: List<JsonObject> = emptyList(),
)
