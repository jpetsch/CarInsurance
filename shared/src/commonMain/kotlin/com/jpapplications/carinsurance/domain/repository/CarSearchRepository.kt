package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria

interface CarSearchRepository {
    suspend fun searchCars(criteria: CarSearchCriteria): List<Car>
}
