package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.lifecycle.ViewModel
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.presentation.screens.carSearch.CarSearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DriverInformationViewModel(
    private val etxCode: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DriverInformationUiState())
    val uiState: StateFlow<DriverInformationUiState> = _uiState.asStateFlow()

    sealed interface DriverInformationEvent {
        data object LoadCarDetails : DriverInformationEvent
    }

    fun onEvent(event: DriverInformationEvent) {
        when (event) {
            is DriverInformationEvent.LoadCarDetails -> onLoadCarDetails()
        }
    }

    private fun onLoadCarDetails() {
        // TODO CarDetails UseCase, Repository, API umsetzen und hier anbinden.
    }

}

data class DriverInformationUiState(
    val car: Car? = null,
)