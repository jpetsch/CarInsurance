package com.jpapplications.carinsurance.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CarSearchViewModel(
    private val searchCarsUseCase: SearchCarsUseCase,
) : ViewModel() {
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

        viewModelScope.launch {
            val result = searchCarsUseCase(
                criteria =
                    CarSearchCriteria(
                        searchTerm = searchTerm,
                        year = uiState.value.year.toIntOrNull(),
                    ),
            )
            _uiState.update { currentState ->
                currentState.copy(
                    cars = result,
                )
            }
        }
    }

    private fun onYearChanged(year: String) {
        val parsedYear = year.toIntOrNull()
        _uiState.update { currentState ->
            currentState.copy(
                year = year,
                hasYearError = parsedYear == null || !CarSearchCriteria.isValidYear(parsedYear),
            )
        }
    }
}

data class CarSearchUiState(
    val searchTerm: String = "",
    val year: String = "",
    val hasYearError: Boolean = false,
    val cars: List<Car> = emptyList(),
)