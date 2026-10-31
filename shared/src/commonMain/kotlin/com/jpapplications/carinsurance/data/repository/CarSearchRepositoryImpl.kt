package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarSearchDataSource
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository

class CarSearchRepositoryImpl(
    private val dataSource: CarSearchDataSource,
) : CarSearchRepository {
    override suspend fun searchCars(criteria: CarSearchCriteria): List<Car> {
        val request =
            CarSearchRequestDto(
                vehicleType = criteria.vehicleType.name,
                searchTerm = criteria.searchTerm,
                year = criteria.year,
            )

        return dataSource.searchCars(request).map { car ->
                Car(
                    matriculationDate = car.matriculationDate,
                    constructionStartYear = car.constructionStartYear,
                    catalogPrice = car.catalogPrice,
                    etxCode = car.etxCode,
                    description = car.description,
                    capacity = car.capacity,
                    model = car.model,
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
                    eMobility = car.eMobility,
                )
            }
    }
}
