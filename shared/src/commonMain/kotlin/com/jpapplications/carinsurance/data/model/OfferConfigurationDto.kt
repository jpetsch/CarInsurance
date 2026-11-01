package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferConfigurationDto(
    val gender: List<String>,
    val schadensfallTyp: List<String>,
    val schadensfallSchuld: List<String>,
    val finanzierung: List<String>,
    val fahrzeugNutzung: List<String>,
    val fahrzeugNutzungAllowedForBusiness: List<String>,
    val vnTyp: List<String>,
    val halterTyp: List<String>,
    val schadenfallYear: List<Int>,
    val maxAge: Int,
)
