package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.jpapplications.carinsurance.presentation.components.CarDetails

@Composable
fun DriverInformationScreen(
    uiState: DriverInformationUiState,
    onEvent: (event: DriverInformationViewModel.DriverInformationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    uiState.car?.let { car ->
        // TODO CarDetails umbauen, so dass es auch ohne Radio Button existieren kann. Die Card reusable machen
        CarDetails(
            car = car,
            onSelected = { /* No action needed */ },
        )
    }
}