package com.jpapplications.carinsurance.domain.model

sealed interface CarSearchResult {
    data class Success(val groups: Map<String, List<Car>>) : CarSearchResult

    data class Failure(val error: CarSearchError) : CarSearchResult
}

enum class CarSearchError {
    NETWORK,
    REQUEST_REJECTED,
    SERVER,
    INVALID_RESPONSE,
}
