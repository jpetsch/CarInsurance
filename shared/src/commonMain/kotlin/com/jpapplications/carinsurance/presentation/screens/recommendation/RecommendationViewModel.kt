package com.jpapplications.carinsurance.presentation.screens.recommendation

import androidx.lifecycle.ViewModel
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecommendationViewModel(
    recommendation: CarRecommendation,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationUiState(recommendation))
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(recommendation = recommendation) }
    }

    sealed interface RecommendationEvent {

    }

    fun onEvent(event: RecommendationEvent) {

    }
}

data class RecommendationUiState(
    val recommendation: CarRecommendation,
)