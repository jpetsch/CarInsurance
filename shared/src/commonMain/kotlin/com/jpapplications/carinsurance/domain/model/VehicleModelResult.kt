package com.jpapplications.carinsurance.domain.model

sealed interface VehicleModelResult {
    data class Success(val car: Car) : VehicleModelResult

    data class Failure(val error: NetworkError) : VehicleModelResult
}
