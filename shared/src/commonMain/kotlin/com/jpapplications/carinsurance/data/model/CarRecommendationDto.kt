package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CarRecommendationDto(
    val gesamtBruttoPraemie: Double,
    val metaData: JsonObject,
    val krf: String,
    val abTestDtos: List<JsonObject>? = null,
    val haftpflicht: JsonObject? = null,
    val kasko: JsonObject? = null,
    val insassenUnfallDto: JsonObject? = null,
    val assistance: Boolean? = null,
)
