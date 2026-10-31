package com.jpapplications.carinsurance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.search_term_label
import carinsurance.shared.generated.resources.year_error
import carinsurance.shared.generated.resources.year_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun CarSearchScreen(
    uiState: CarSearchUiState,
    onEvent: (event: CarSearchViewModel.CarSearchEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.year,
            onValueChange = { value ->
                // TODO Big numbers cause a NumberFormatException. Set a max.
                if (value.any { !it.isDigit() }) {
                    return@OutlinedTextField
                }
                onEvent(CarSearchViewModel.CarSearchEvent.YearChanged(value))
            },
            label = { Text(stringResource(Res.string.year_label)) },
            isError = uiState.hasYearError,
            supportingText = {
                // TODO Mit spezifischeren Texten ergänzen wenn zeit übrig
                Text(stringResource(Res.string.year_error))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.searchTerm,
            onValueChange = { onEvent(CarSearchViewModel.CarSearchEvent.SearchTermChanged(it)) },
            label = { Text(stringResource(Res.string.search_term_label)) },
            singleLine = true,
        )
    }
}

@Preview
@Composable
fun CarSearchScreenPreview() {
    CarSearchScreen(
        modifier = Modifier
            .background(color = Color.White),
        uiState = CarSearchUiState(),
        onEvent = {},
    )
}