package com.jpapplications.carinsurance.domain.model

data class CarSearchCriteria(
    val searchTerm: String,
    val year: Int?,
    val vehicleType: VehicleType = VehicleType.CAR,
) {
    companion object {
        const val MIN_YEAR = 1975
        const val MAX_YEAR = 2033

        fun isValidYear(year: Int): Boolean = year in MIN_YEAR..MAX_YEAR
    }
}
