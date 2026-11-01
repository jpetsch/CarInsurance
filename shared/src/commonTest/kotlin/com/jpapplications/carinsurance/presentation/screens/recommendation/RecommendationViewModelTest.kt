package com.jpapplications.carinsurance.presentation.screens.recommendation

import com.jpapplications.carinsurance.domain.recommendationFixture
import kotlin.test.Test
import kotlin.test.assertEquals

class RecommendationViewModelTest {
    @Test
    fun initialStateContainsPassedRecommendation() {
        val recommendation = recommendationFixture()

        val viewModel = RecommendationViewModel(recommendation)

        assertEquals(recommendation, viewModel.uiState.value.recommendation)
    }
}
