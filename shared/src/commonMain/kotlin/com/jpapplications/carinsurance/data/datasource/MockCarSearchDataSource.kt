package com.jpapplications.carinsurance.data.datasource

import com.jpapplications.carinsurance.data.model.CarSearchDto
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto

class MockCarSearchDataSource : CarSearchDataSource {
    override suspend fun searchCars(request: CarSearchRequestDto): List<CarSearchDto> {
        val searchTerm = request.searchTerm.trim()

        return cars.filter { car ->
            (searchTerm.isEmpty() ||
                car.description.contains(searchTerm, ignoreCase = true) ||
                car.brand.contains(searchTerm, ignoreCase = true) ||
                car.model.contains(searchTerm, ignoreCase = true)) &&
                (request.year == null || car.constructionStartYear == request.year)
        }
    }

    private companion object {
        val cars =
            listOf(
                CarSearchDto(
                    matriculationDate = 2020,
                    constructionStartYear = 2020,
                    catalogPrice = 63200,
                    etxCode = 102226758,
                    description = "Golf 2.0 TSI R DSG 4Motion",
                    capacity = 1998,
                    doors = 5,
                    seats = 5,
                    emptyLoad = 1569,
                    horsePower = 320,
                    enginePowerInKw = 235,
                    vehicleType = "PKW",
                    vehicleClass = 30003,
                    constructionType = "LIMOUSINE_SEDAN",
                    fuel = "BENZIN_BLEIFREI",
                    brand = "VW",
                    model = "GOLF VIII BENZIN 5 Türer Modell 2020-",
                    eMobility = false,
                ),
                CarSearchDto(
                    matriculationDate = 2018,
                    constructionStartYear = 2018,
                    catalogPrice = 35900,
                    etxCode = 102226759,
                    description = "Golf 1.5 TSI DSG",
                    capacity = 1498,
                    doors = 5,
                    seats = 5,
                    emptyLoad = 1305,
                    horsePower = 150,
                    enginePowerInKw = 110,
                    vehicleType = "PKW",
                    vehicleClass = 30003,
                    constructionType = "LIMOUSINE_SEDAN",
                    fuel = "BENZIN_BLEIFREI",
                    brand = "VW",
                    model = "GOLF VII BENZIN 5 Türer Modell 2017-",
                    eMobility = false,
                ),
                CarSearchDto(
                    matriculationDate = 2022,
                    constructionStartYear = 2022,
                    catalogPrice = 54900,
                    etxCode = 102226760,
                    description = "3er 320d Steptronic",
                    capacity = 1995,
                    doors = 4,
                    seats = 5,
                    emptyLoad = 1615,
                    horsePower = 190,
                    enginePowerInKw = 140,
                    vehicleType = "PKW",
                    vehicleClass = 30003,
                    constructionType = "LIMOUSINE_SEDAN",
                    fuel = "DIESEL",
                    brand = "BMW",
                    model = "3ER DIESEL Modell 2019-",
                    eMobility = false,
                ),
            )
    }
}
