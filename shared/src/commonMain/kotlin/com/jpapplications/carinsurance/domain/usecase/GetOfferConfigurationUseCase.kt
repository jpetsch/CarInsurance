package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.repository.OfferConfigurationRepository

class GetOfferConfigurationUseCase(
    private val repository: OfferConfigurationRepository,
) {
    suspend operator fun invoke(): OfferConfigurationResult =
        repository.getOfferConfiguration()
}
