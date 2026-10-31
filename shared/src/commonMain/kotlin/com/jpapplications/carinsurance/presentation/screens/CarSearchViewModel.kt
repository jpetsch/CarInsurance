package com.jpapplications.carinsurance.presentation.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarSearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CarSearchUiState())
    val uiState: StateFlow<CarSearchUiState> = _uiState.asStateFlow()

    sealed interface CarSearchEvent {
        data class SearchTermChanged(
            val searchTerm: String,
        ) : CarSearchEvent

        data class YearChanged(
            val year: String,
        ) : CarSearchEvent
    }

    fun onEvent(event: CarSearchEvent) {
        when (event) {
            is CarSearchEvent.SearchTermChanged -> onSearchTermChanged(event.searchTerm)
            is CarSearchEvent.YearChanged -> onYearChanged(event.year)
        }
    }

    private fun onSearchTermChanged(searchTerm: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchTerm = searchTerm,
            )
        }
    }

    private fun onYearChanged(year: String) {
        _uiState.update { currentState ->
            currentState.copy(
                year = year,
                hasYearError = validateYearInput(year),
            )
        }
    }

    private fun validateYearInput(yearString: String): Boolean {
        if (yearString.any { !it.isDigit() }) {
            return false
        }
        val year = yearString.toInt()
        return year !in 2033 downTo 1975
    }
}

data class CarSearchUiState(
    val searchTerm: String = "",
    val year: String = "", // TODO Bei übergabe an domain in Int umwandeln?
    val hasYearError: Boolean = false,
)