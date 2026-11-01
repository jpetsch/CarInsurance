package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jpapplications.carinsurance.presentation.components.CarDetails

@Composable
fun DriverInformationScreen(
    uiState: DriverInformationUiState,
    onEvent: (event: DriverInformationViewModel.DriverInformationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Dein Fahrzeug",
            style = typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        uiState.car?.let { car ->
            CarDetails(
                car = car,
            )
        }
    }
}