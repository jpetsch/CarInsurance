package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CarRecommendationDto(
    val gesamtBruttoPraemie: Double,
    val metaData: RecommendationMetaDataDto,
    val krf: String,
    val abTestDtos: List<RecommendationAbTestDto>? = null,
    val haftpflicht: RecommendationLiabilityDto? = null,
    val kasko: RecommendationHullDto? = null,
    val insassenUnfallDto: RecommendationPassengerAccidentDto? = null,
    val assistance: Boolean? = null,
)

@Serializable
data class RecommendationMetaDataDto(
    val uwfLiability: String,
    val cffLiability: String,
    val uwfAssistance: String,
    val cffAssistance: String,
    val uwfHull: String,
    val cffHullTk: String,
    val cffHullVk: String,
    val uwfPassenger: String,
    val cffPassenger: String,
    val keepOnChanges: Boolean,
)

@Serializable
data class RecommendationAbTestDto(
    val jiraTicketId: String,
    val groupName: String,
)

@Serializable
data class RecommendationLiabilityDto(
    val bonusschutz: Boolean,
    val gfverzicht: Boolean,
    val bonusMalusStufe: Int,
    val bonusMalusProzent: Int,
    val haftpflichtSelbstbehalt: Int,
)

@Serializable
data class RecommendationHullDto(
    val kaskoArt: String,
    val vollkaskoSelbstbehalt: Int,
    val teilkaskoSelbstbehalt: Int,
    val entschaedigungsart: String,
    val parkschaeden: String,
    val kaskoGlasbruch: String,
    val bonusschutz: Boolean,
    val persoenlicheEffekten: String,
    val bonusMalusStufe: Int,
    val bonusMalusProzent: Int,
)

@Serializable
data class RecommendationPassengerAccidentDto(
    val insassenUnfallPersonenkreis: String,
    val spitaltaggeld: Int,
    val taggeld: Int,
    val heilungskosten: Boolean,
    val invaliditaetskapital: Int,
    val todesfallkapital: Int,
    val haustiere: String,
)
