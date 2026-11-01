package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.OfferConfigurationDto
import com.jpapplications.carinsurance.domain.model.OfferConfiguration

internal fun OfferConfigurationDto.toDomain() =
    OfferConfiguration(
        gender = gender,
        schadensfallTyp = schadensfallTyp,
        schadensfallSchuld = schadensfallSchuld,
        finanzierung = finanzierung,
        fahrzeugNutzung = fahrzeugNutzung,
        fahrzeugNutzungAllowedForBusiness = fahrzeugNutzungAllowedForBusiness,
        vnTyp = vnTyp,
        halterTyp = halterTyp,
        schadenfallYear = schadenfallYear,
        maxAge = maxAge,
    )
