package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult

interface CarRecommendationRepository {
    suspend fun getRecommendation(
        request: CarRecommendationRequest,
        language: String,
    ): CarRecommendationResult
}
