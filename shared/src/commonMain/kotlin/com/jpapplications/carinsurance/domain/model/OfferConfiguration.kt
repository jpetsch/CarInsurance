package com.jpapplications.carinsurance.domain.model

data class OfferConfiguration(
    val gender: List<Gender>,
    val schadensfallTyp: List<DamageType>,
    val schadensfallSchuld: List<DamageFault>,
    val finanzierung: List<String>,
    val fahrzeugNutzung: List<VehicleUsage>,
    val fahrzeugNutzungAllowedForBusiness: List<VehicleUsage>,
    val vnTyp: List<String>,
    val halterTyp: List<String>,
    val schadenfallYear: List<Int>,
    val maxAge: Int,
)
