package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.CarRecommendationDto
import com.jpapplications.carinsurance.data.model.CarRecommendationRequestDto
import com.jpapplications.carinsurance.data.model.RecommendationAbTestDto
import com.jpapplications.carinsurance.data.model.RecommendationClaimDto
import com.jpapplications.carinsurance.data.model.RecommendationCityDto
import com.jpapplications.carinsurance.data.model.RecommendationDiscountDataDto
import com.jpapplications.carinsurance.data.model.RecommendationDriverDto
import com.jpapplications.carinsurance.data.model.RecommendationHullDto
import com.jpapplications.carinsurance.data.model.RecommendationLiabilityDto
import com.jpapplications.carinsurance.data.model.RecommendationMetaDataDto
import com.jpapplications.carinsurance.data.model.RecommendationPassengerAccidentDto
import com.jpapplications.carinsurance.data.model.RecommendationVehicleDto
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.RecommendationAbTest
import com.jpapplications.carinsurance.domain.model.RecommendationClaim
import com.jpapplications.carinsurance.domain.model.RecommendationCity
import com.jpapplications.carinsurance.domain.model.RecommendationDiscountData
import com.jpapplications.carinsurance.domain.model.RecommendationDriver
import com.jpapplications.carinsurance.domain.model.RecommendationHull
import com.jpapplications.carinsurance.domain.model.RecommendationLiability
import com.jpapplications.carinsurance.domain.model.RecommendationMetaData
import com.jpapplications.carinsurance.domain.model.RecommendationPassengerAccident
import com.jpapplications.carinsurance.domain.model.RecommendationVehicle

internal fun RecommendationCityDto.toDomain() =
    RecommendationCity(
        plz = plz,
        name = name,
        kanton = kanton,
    )

private fun RecommendationCity.toDto() =
    RecommendationCityDto(
        plz = plz,
        name = name,
        kanton = kanton,
    )

internal fun CarRecommendationRequest.toDto() =
    CarRecommendationRequestDto(
        rabattRelevanteDaten = rabattRelevanteDaten.toDto(),
        versicherungsBeginn = versicherungsBeginn,
        zahlungsintervall = zahlungsintervall,
        halterTyp = halterTyp,
        fahrzeug = fahrzeug.toDto(),
        fahrzeugLenker = fahrzeugLenker.toDto(),
        abTestDtos = abTestDtos?.map(RecommendationAbTest::toDto),
    )

private fun RecommendationDiscountData.toDto() = RecommendationDiscountDataDto()

private fun RecommendationAbTest.toDto() =
    RecommendationAbTestDto(
        jiraTicketId = jiraTicketId,
        groupName = groupName,
    )

private fun RecommendationClaim.toDto() =
    RecommendationClaimDto(
        schadensfallTyp = schadensfallTyp.apiCode,
        schadenfallYear = schadenfallYear,
        schadensfallSchuld = schadensfallSchuld.apiCode,
        summe = summe,
    )

private fun RecommendationVehicle.toDto() =
    RecommendationVehicleDto(
        matriculationDate = matriculationDate,
        constructionStartYear = car.constructionStartYear,
        catalogPrice = car.catalogPrice,
        etxCode = car.etxCode,
        description = car.description,
        capacity = car.capacity,
        doors = car.doors,
        seats = car.seats,
        emptyLoad = car.emptyLoad,
        horsePower = car.horsePower,
        enginePowerInKw = car.enginePowerInKw,
        vehicleType = car.vehicleType,
        vehicleClass = car.vehicleClass,
        constructionType = car.constructionType,
        fuel = car.fuel,
        brand = car.brand,
        model = car.model,
        eMobility = car.eMobility,
        newValueAccessories = newValueAccessories,
        financing = financing,
        vehicleUsage = vehicleUsage.apiCode,
    )

private fun RecommendationDriver.toDto() =
    RecommendationDriverDto(
        geschlecht = geschlecht.apiCode,
        geburtsdatum = geburtsdatum,
        nationalitaet = nationalitaet,
        stadt = stadt.toDto(),
        jahrFahrpruefungPkw = jahrFahrpruefungPkw,
        schadensfaelle = schadensfaelle.map(RecommendationClaim::toDto),
    )

private fun RecommendationAbTestDto.toDomain() =
    RecommendationAbTest(
        jiraTicketId = jiraTicketId,
        groupName = groupName,
    )

private fun RecommendationMetaDataDto.toDomain() =
    RecommendationMetaData(
        uwfLiability = uwfLiability,
        cffLiability = cffLiability,
        uwfAssistance = uwfAssistance,
        cffAssistance = cffAssistance,
        uwfHull = uwfHull,
        cffHullTk = cffHullTk,
        cffHullVk = cffHullVk,
        uwfPassenger = uwfPassenger,
        cffPassenger = cffPassenger,
        keepOnChanges = keepOnChanges,
    )

private fun RecommendationLiabilityDto.toDomain() =
    RecommendationLiability(
        bonusschutz = bonusschutz,
        gfverzicht = gfverzicht,
        bonusMalusStufe = bonusMalusStufe,
        bonusMalusProzent = bonusMalusProzent,
        haftpflichtSelbstbehalt = haftpflichtSelbstbehalt,
    )

private fun RecommendationHullDto.toDomain() =
    RecommendationHull(
        kaskoArt = kaskoArt,
        vollkaskoSelbstbehalt = vollkaskoSelbstbehalt,
        teilkaskoSelbstbehalt = teilkaskoSelbstbehalt,
        entschaedigungsart = entschaedigungsart,
        parkschaeden = parkschaeden,
        kaskoGlasbruch = kaskoGlasbruch,
        bonusschutz = bonusschutz,
        persoenlicheEffekten = persoenlicheEffekten,
        bonusMalusStufe = bonusMalusStufe,
        bonusMalusProzent = bonusMalusProzent,
    )

private fun RecommendationPassengerAccidentDto.toDomain() =
    RecommendationPassengerAccident(
        insassenUnfallPersonenkreis = insassenUnfallPersonenkreis,
        spitaltaggeld = spitaltaggeld,
        taggeld = taggeld,
        heilungskosten = heilungskosten,
        invaliditaetskapital = invaliditaetskapital,
        todesfallkapital = todesfallkapital,
        haustiere = haustiere,
    )

internal fun CarRecommendationDto.toDomain() =
    CarRecommendation(
        gesamtBruttoPraemie = gesamtBruttoPraemie,
        metaData = metaData.toDomain(),
        krf = krf,
        abTestDtos = abTestDtos?.map(RecommendationAbTestDto::toDomain),
        haftpflicht = haftpflicht?.toDomain(),
        kasko = kasko?.toDomain(),
        insassenUnfallDto = insassenUnfallDto?.toDomain(),
        assistance = assistance,
    )
