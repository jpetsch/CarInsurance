package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DriverInformationViewModel(
    private val etxCode: Int,
    private val getVehicleModelUseCase: GetVehicleModelUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DriverInformationUiState())
    val uiState: StateFlow<DriverInformationUiState> = _uiState.asStateFlow()

    init {
        loadCarDetails()
    }

    sealed interface DriverInformationEvent {
    }

    fun onEvent(event: DriverInformationEvent) {

    }

    private fun loadCarDetails() {
        viewModelScope.launch {
            when(val result = getVehicleModelUseCase(etxCode)) {
                is VehicleModelResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            car = result.car,
                        )
                    }
                }
                is VehicleModelResult.Failure -> TODO("Not yet implemented")
            }
        }
    }

}

data class DriverInformationUiState(
    val car: Car? = null,
)