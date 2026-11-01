package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.OfferConfigurationDto
import com.jpapplications.carinsurance.domain.model.DamageFault
import com.jpapplications.carinsurance.domain.model.DamageType
import com.jpapplications.carinsurance.domain.model.Gender
import com.jpapplications.carinsurance.domain.model.VehicleUsage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OfferConfigurationMapperTest {
    @Test
    fun mapsAllSupportedCodesToTypedValues() {
        assertEquals(
            listOf(
                "WITH_BONUSSCHUTZ",
                "YES_SELF_PAYMENT",
                "YES_INSURANCE_PAYMENT",
                "NO_SELF_PAYMENT",
                "NO_INSURANCE_PAYMENT",
            ),
            DamageFault.entries.map(DamageFault::apiCode),
        )

        val configuration =
            OfferConfigurationDto(
                gender = listOf("MALE", "NEW_GENDER"),
                schadensfallTyp = DamageType.entries.map(DamageType::apiCode),
                schadensfallSchuld = DamageFault.entries.map(DamageFault::apiCode),
                finanzierung = emptyList(),
                fahrzeugNutzung = listOf("PRIVAT", "NEW_USAGE"),
                fahrzeugNutzungAllowedForBusiness = listOf("BUSINESS"),
                vnTyp = emptyList(),
                halterTyp = emptyList(),
                schadenfallYear = emptyList(),
                maxAge = 100,
            ).toDomain()

        assertEquals(
            listOf(Gender.Known.MALE, Gender.Unknown("NEW_GENDER")),
            configuration.gender,
        )
        assertEquals(DamageType.entries, configuration.schadensfallTyp)
        assertEquals(DamageFault.entries, configuration.schadensfallSchuld)
        assertEquals(
            listOf(VehicleUsage.Known.PRIVATE, VehicleUsage.Unknown("NEW_USAGE")),
            configuration.fahrzeugNutzung,
        )
        assertEquals(
            listOf(VehicleUsage.Known.BUSINESS),
            configuration.fahrzeugNutzungAllowedForBusiness,
        )
    }

    @Test
    fun rejectsUnsupportedDamageTypeCodes() {
        assertFailsWith<UnsupportedOfferConfigurationValueException> {
            OfferConfigurationDto(
                gender = emptyList(),
                schadensfallTyp = listOf("NEW_DAMAGE"),
                schadensfallSchuld = emptyList(),
                finanzierung = emptyList(),
                fahrzeugNutzung = emptyList(),
                fahrzeugNutzungAllowedForBusiness = emptyList(),
                vnTyp = emptyList(),
                halterTyp = emptyList(),
                schadenfallYear = emptyList(),
                maxAge = 100,
            ).toDomain()
        }

    }

    @Test
    fun rejectsUnsupportedDamageFaultCodes() {
        assertFailsWith<UnsupportedOfferConfigurationValueException> {
            OfferConfigurationDto(
                gender = emptyList(),
                schadensfallTyp = emptyList(),
                schadensfallSchuld = listOf("NEW_FAULT"),
                finanzierung = emptyList(),
                fahrzeugNutzung = emptyList(),
                fahrzeugNutzungAllowedForBusiness = emptyList(),
                vnTyp = emptyList(),
                halterTyp = emptyList(),
                schadenfallYear = emptyList(),
                maxAge = 100,
            ).toDomain()
        }
    }
}
