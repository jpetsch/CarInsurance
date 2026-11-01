package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult
import com.jpapplications.carinsurance.domain.repository.CarRecommendationRepository

class GetCarRecommendationUseCase(
    private val repository: CarRecommendationRepository,
) {
    suspend operator fun invoke(
        request: CarRecommendationRequest,
        language: String = "de",
    ): CarRecommendationResult = repository.getRecommendation(request, language)
}
