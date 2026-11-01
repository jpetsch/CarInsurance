package com.jpapplications.carinsurance.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CarSearchRequestDto(
    val vehicleType: String,
    val searchTerm: String,
    val year: Int?,
)
