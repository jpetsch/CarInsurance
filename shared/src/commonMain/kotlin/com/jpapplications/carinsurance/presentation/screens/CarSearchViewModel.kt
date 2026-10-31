package com.jpapplications.carinsurance.presentation.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarSearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CarSearchUiState())
    val uiState: StateFlow<CarSearchUiState> = _uiState.asStateFlow()

    sealed interface CarSearchEvent {
        data class SearchTermChanged(
            val searchTerm: String,
        ) : CarSearchEvent
    }

    fun onEvent(event: CarSearchEvent) {
        when (event) {
            is CarSearchEvent.SearchTermChanged -> Unit
        }
    }
}


data class CarSearchUiState(
    val searchTerm: String = "",
)