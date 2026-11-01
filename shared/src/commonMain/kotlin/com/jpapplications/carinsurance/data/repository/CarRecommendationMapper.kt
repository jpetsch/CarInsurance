package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.CarRecommendationDto
import com.jpapplications.carinsurance.data.model.CarRecommendationRequestDto
import com.jpapplications.carinsurance.data.model.RecommendationDriverDto
import com.jpapplications.carinsurance.data.model.RecommendationVehicleDto
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.RecommendationCity
import com.jpapplications.carinsurance.domain.model.RecommendationDriver
import com.jpapplications.carinsurance.domain.model.RecommendationVehicle
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

internal fun JsonObject.toRecommendationCity(): RecommendationCity? {
    val name =
        listOf("name", "city", "cityName", "ort", "ortschaft", "stadt", "gemeinde", "locality")
            .firstNotNullOfOrNull { key ->
                (this[key] as? JsonPrimitive)
                    ?.takeIf { it.isString }
                    ?.contentOrNull
                    ?.takeIf(String::isNotBlank)
            } ?: return null
    return RecommendationCity(name = name, data = this)
}

internal fun CarRecommendationRequest.toDto() =
    CarRecommendationRequestDto(
        rabattRelevanteDaten = rabattRelevanteDaten,
        versicherungsBeginn = versicherungsBeginn,
        zahlungsintervall = zahlungsintervall,
        halterTyp = halterTyp,
        fahrzeug = fahrzeug.toDto(),
        fahrzeugLenker = fahrzeugLenker.toDto(),
        abTestDtos = abTestDtos,
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
        vehicleUsage = vehicleUsage,
    )

private fun RecommendationDriver.toDto() =
    RecommendationDriverDto(
        geschlecht = geschlecht,
        geburtsdatum = geburtsdatum,
        nationalitaet = nationalitaet,
        stadt = stadt,
        jahrFahrpruefungPkw = jahrFahrpruefungPkw,
        schadensfaelle = schadensfaelle,
    )

internal fun CarRecommendationDto.toDomain() =
    CarRecommendation(
        gesamtBruttoPraemie = gesamtBruttoPraemie,
        haftpflicht = haftpflicht,
        kasko = kasko,
        insassenUnfallDto = insassenUnfallDto,
    )
