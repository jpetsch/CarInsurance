package com.jpapplications.carinsurance.domain.model

data class OfferConfiguration(
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
