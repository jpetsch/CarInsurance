package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository

class SearchCarsUseCase(
    private val repository: CarSearchRepository,
) {
    // TODO Use Result instead of List directly.
    suspend operator fun invoke(criteria: CarSearchCriteria): List<Car> =
        repository.searchCars(criteria)
}
