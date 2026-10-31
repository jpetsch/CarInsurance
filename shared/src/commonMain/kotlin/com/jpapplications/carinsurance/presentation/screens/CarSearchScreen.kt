package com.jpapplications.carinsurance.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CarSearchScreen(
    uiState: CarSearchUiState,
    onEvent: (event: CarSearchViewModel.CarSearchEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Car Search Screen",
        )
    }
}

@Preview
@Composable
fun CarSearchScreenPreview() {
    CarSearchScreen(
        uiState = CarSearchUiState(),
        onEvent = {},
    )
}