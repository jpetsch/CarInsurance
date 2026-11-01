package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.OfferConfigurationDto
import com.jpapplications.carinsurance.domain.model.DamageFault
import com.jpapplications.carinsurance.domain.model.DamageType
import com.jpapplications.carinsurance.domain.model.Gender
import com.jpapplications.carinsurance.domain.model.OfferConfiguration
import com.jpapplications.carinsurance.domain.model.VehicleUsage

internal fun OfferConfigurationDto.toDomain() =
    OfferConfiguration(
        gender = gender.map(String::toGender),
        schadensfallTyp = schadensfallTyp.map(String::toDamageType),
        schadensfallSchuld = schadensfallSchuld.map(String::toDamageFault),
        finanzierung = finanzierung,
        fahrzeugNutzung = fahrzeugNutzung.map(String::toVehicleUsage),
        fahrzeugNutzungAllowedForBusiness =
            fahrzeugNutzungAllowedForBusiness.map(String::toVehicleUsage),
        vnTyp = vnTyp,
        halterTyp = halterTyp,
        schadenfallYear = schadenfallYear,
        maxAge = maxAge,
    )

private fun String.toGender(): Gender =
    when (uppercase()) {
        "MALE" -> Gender.Known.MALE
        "FEMALE" -> Gender.Known.FEMALE
        else -> Gender.Unknown(this)
    }

private fun String.toVehicleUsage(): VehicleUsage =
    VehicleUsage.Known.entries.firstOrNull { it.apiCode == uppercase() } ?: VehicleUsage.Unknown(this)

private fun String.toDamageType(): DamageType =
    DamageType.entries.firstOrNull { it.apiCode == uppercase() }
        ?: throw UnsupportedOfferConfigurationValueException("damage type", this)

private fun String.toDamageFault(): DamageFault =
    DamageFault.entries.firstOrNull { it.apiCode == uppercase() }
        ?: throw UnsupportedOfferConfigurationValueException("damage fault", this)
