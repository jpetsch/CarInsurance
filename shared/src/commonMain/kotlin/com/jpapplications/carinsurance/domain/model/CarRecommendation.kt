package com.jpapplications.carinsurance.domain.model

import kotlinx.serialization.Serializable

data class CarRecommendationRequest(
    val rabattRelevanteDaten: RecommendationDiscountData = RecommendationDiscountData(),
    val versicherungsBeginn: String,
    val zahlungsintervall: String,
    val halterTyp: String,
    val fahrzeug: RecommendationVehicle,
    val fahrzeugLenker: RecommendationDriver,
    val abTestDtos: List<RecommendationAbTest>? = null,
)

class RecommendationDiscountData

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
    val stadt: RecommendationCity,
    val jahrFahrpruefungPkw: Int,
    val schadensfaelle: List<RecommendationClaim> = emptyList(),
)

data class RecommendationCity(
    val plz: Int,
    val name: String,
    val kanton: String,
)

data class RecommendationClaim(
    val schadensfallTyp: String,
    val schadenfallYear: Int,
    val schadensfallSchuld: String,
    val summe: Double,
)

@Serializable
data class RecommendationAbTest(
    val jiraTicketId: String,
    val groupName: String,
)

sealed interface CityLookupResult {
    data class Success(val city: RecommendationCity) : CityLookupResult

    data class Failure(val error: NetworkError) : CityLookupResult
}

@Serializable
data class RecommendationMetaData(
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
data class RecommendationLiability(
    val bonusschutz: Boolean,
    val gfverzicht: Boolean,
    val bonusMalusStufe: Int,
    val bonusMalusProzent: Int,
    val haftpflichtSelbstbehalt: Int,
)

@Serializable
data class RecommendationHull(
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
data class RecommendationPassengerAccident(
    val insassenUnfallPersonenkreis: String,
    val spitaltaggeld: Int,
    val taggeld: Int,
    val heilungskosten: Boolean,
    val invaliditaetskapital: Int,
    val todesfallkapital: Int,
    val haustiere: String,
)

@Serializable
data class CarRecommendation(
    val gesamtBruttoPraemie: Double,
    val metaData: RecommendationMetaData,
    val krf: String,
    val abTestDtos: List<RecommendationAbTest>? = null,
    val haftpflicht: RecommendationLiability? = null,
    val kasko: RecommendationHull? = null,
    val insassenUnfallDto: RecommendationPassengerAccident? = null,
    val assistance: Boolean? = null,
)

sealed interface CarRecommendationResult {
    data class Success(val recommendation: CarRecommendation) : CarRecommendationResult

    data class Failure(val error: NetworkError) : CarRecommendationResult
}
