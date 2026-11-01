package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.model.CarDto
import com.jpapplications.carinsurance.domain.model.Car

internal fun CarDto.toDomain() =
    Car(
        matriculationDate = matriculationDate,
        constructionStartYear = constructionStartYear,
        catalogPrice = catalogPrice,
        etxCode = etxCode,
        description = description,
        capacity = capacity,
        model = model,
        doors = doors,
        seats = seats,
        emptyLoad = emptyLoad,
        horsePower = horsePower,
        enginePowerInKw = enginePowerInKw,
        vehicleType = vehicleType,
        vehicleClass = vehicleClass,
        constructionType = constructionType,
        fuel = fuel,
        brand = brand,
        eMobility = eMobility,
    )
