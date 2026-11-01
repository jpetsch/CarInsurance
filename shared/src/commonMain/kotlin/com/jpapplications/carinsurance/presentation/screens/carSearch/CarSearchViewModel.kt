package com.jpapplications.carinsurance.presentation.screens.carSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CarSearchViewModel(
    private val searchCarsUseCase: SearchCarsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CarSearchUiState())
    val uiState: StateFlow<CarSearchUiState> = _uiState.asStateFlow()
    private val _carModelSelected: MutableSharedFlow<Int> = MutableSharedFlow()
    val carModelSelected: SharedFlow<Int> = _carModelSelected.asSharedFlow()

    private var searchJob: Job? = null

    sealed interface CarSearchEvent {
        data class SearchTermChanged(
            val searchTerm: String,
        ) : CarSearchEvent

        data class YearChanged(
            val year: String,
        ) : CarSearchEvent

        data class CarSelected(
            val carGroup: Map.Entry<String, List<Car>>,
        ) : CarSearchEvent

        data class CarEntrySelected(
            val etxCode: Int,
        ) : CarSearchEvent
    }

    fun onEvent(event: CarSearchEvent) {
        when (event) {
            is CarSearchEvent.SearchTermChanged -> onSearchTermChanged(event.searchTerm)
            is CarSearchEvent.YearChanged -> onYearChanged(event.year)
            is CarSearchEvent.CarSelected -> onCarGroupSelected(event.carGroup)
            is CarSearchEvent.CarEntrySelected -> onCarEntrySelected(event.etxCode)
        }
    }

    private fun onSearchTermChanged(searchTerm: String) {
        searchJob?.cancel()

        _uiState.update { currentState ->
            currentState.copy(
                searchTerm = searchTerm,
                carGroups = emptyMap(),
                selectedCarGroup = null,
                selectedCar = null,
                searchError = null,
            )
        }

        vehicleSearch()
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

    private fun onCarGroupSelected(carGroup: Map.Entry<String, List<Car>>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCarGroup = carGroup,
                selectedCar = null,
            )
        }
    }

    private fun onCarEntrySelected(etxCode: Int) {
        viewModelScope.launch {
            _carModelSelected.emit(etxCode)
        }
    }

    private fun vehicleSearch() {
        val searchTerm = uiState.value.searchTerm

        if (searchTerm.length !in 2..255) return

        val year = uiState.value.year.toIntOrNull()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS.milliseconds)

            val result = searchCarsUseCase(
                criteria =
                    CarSearchCriteria(
                        searchTerm = searchTerm,
                        year = year,
                    ),
            )
            when (result) {
                is CarSearchResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            carGroups = result.groups,
                            searchError = null,
                        )
                    }
                }
                is CarSearchResult.Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            carGroups = emptyMap(),
                            searchError = result.error,
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 500L
    }
}

data class CarSearchUiState(
    val searchTerm: String = "",
    val year: String = "",
    val hasYearError: Boolean = false,
    val carGroups: Map<String, List<Car>> = emptyMap(),
    val selectedCarGroup: Map.Entry<String, List<Car>>? = null,
    val selectedCar: Car? = null,
    val searchError: NetworkError? = null,
)