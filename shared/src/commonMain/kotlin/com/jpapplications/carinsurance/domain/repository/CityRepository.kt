package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.CityLookupResult

interface CityRepository {
    suspend fun getCity(postalCode: String): CityLookupResult
}
