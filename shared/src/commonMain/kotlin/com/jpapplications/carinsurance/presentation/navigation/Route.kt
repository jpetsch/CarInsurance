package com.jpapplications.carinsurance.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import kotlinx.serialization.Serializable

@Serializable
sealed class Route : NavKey {
    @Serializable
    data object CarSearch : Route(), NavKey

    @Serializable
    data class DriverInformation(val etxCode: Int) : Route(), NavKey

    @Serializable
    data class Recommendation(val carRecommendation: CarRecommendation) : Route(), NavKey
}
