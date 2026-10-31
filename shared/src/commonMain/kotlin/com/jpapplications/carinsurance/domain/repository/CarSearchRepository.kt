package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult

interface CarSearchRepository {
    suspend fun searchCars(criteria: CarSearchCriteria): CarSearchResult
}
