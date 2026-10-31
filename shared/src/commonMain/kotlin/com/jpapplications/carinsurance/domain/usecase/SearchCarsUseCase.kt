package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository

class SearchCarsUseCase(
    private val repository: CarSearchRepository,
) {
    suspend operator fun invoke(criteria: CarSearchCriteria): CarSearchResult =
        repository.searchCars(criteria)
}
