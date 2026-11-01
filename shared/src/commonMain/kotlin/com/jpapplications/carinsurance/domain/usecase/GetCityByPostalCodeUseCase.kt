package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.repository.CityRepository

class GetCityByPostalCodeUseCase(
    private val repository: CityRepository,
) {
    suspend operator fun invoke(postalCode: String): CityLookupResult =
        repository.getCity(postalCode)
}
