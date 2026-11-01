package com.jpapplications.carinsurance.domain.model

sealed interface OfferConfigurationResult {
    data class Success(val configuration: OfferConfiguration) : OfferConfigurationResult

    data class Failure(val error: NetworkError) : OfferConfigurationResult
}
