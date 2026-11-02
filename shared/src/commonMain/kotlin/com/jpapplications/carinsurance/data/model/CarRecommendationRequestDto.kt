package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CarRecommendationRequestDto(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val rabattRelevanteDaten: RecommendationDiscountDataDto = RecommendationDiscountDataDto(),
    val versicherungsBeginn: String,
    val zahlungsintervall: String,
    val halterTyp: String,
    val fahrzeug: RecommendationVehicleDto,
    val fahrzeugLenker: RecommendationDriverDto,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val abTestDtos: List<RecommendationAbTestDto>? = null,
)

@Serializable
internal class RecommendationDiscountDataDto

@Serializable
internal data class RecommendationVehicleDto(
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
internal data class RecommendationDriverDto(
    val geschlecht: String,
    val geburtsdatum: String,
    val nationalitaet: String,
    val stadt: RecommendationCityDto,
    val jahrFahrpruefungPkw: Int,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val schadensfaelle: List<RecommendationClaimDto> = emptyList(),
)

@Serializable
internal data class RecommendationCityDto(
    val plz: Int,
    val name: String,
    val kanton: String,
)

@Serializable
internal data class RecommendationClaimDto(
    val schadensfallTyp: String,
    @SerialName("jahr")
    val schadenfallYear: Int,
    val schadensfallSchuld: String,
    @SerialName("schadensfallBetrag")
    val summe: Double,
)
