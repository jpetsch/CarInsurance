package com.jpapplications.carinsurance.presentation.screens.carSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.search_invalid_response_error
import carinsurance.shared.generated.resources.search_network_error
import carinsurance.shared.generated.resources.search_request_error
import carinsurance.shared.generated.resources.search_server_error
import carinsurance.shared.generated.resources.year_error
import carinsurance.shared.generated.resources.year_label
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.presentation.components.CarDetails
import com.jpapplications.carinsurance.presentation.components.CarDetailsMode
import com.jpapplications.carinsurance.presentation.components.SearchDropdown
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
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.year,
            onValueChange = { value ->
                if (value.any { !it.isDigit() }) {
                    return@OutlinedTextField
                }
                onEvent(CarSearchViewModel.CarSearchEvent.YearChanged(value))
            },
            label = { Text(stringResource(Res.string.year_label)) },
            isError = uiState.hasYearError,
            supportingText =
                if (uiState.hasYearError) {
                    { Text(stringResource(Res.string.year_error)) }
                } else {
                    null
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SearchDropdown(
            searchTerm = uiState.searchTerm,
            carGroups = uiState.carGroups,
            onSearchTermChanged = {
                onEvent(CarSearchViewModel.CarSearchEvent.SearchTermChanged(it))
            },
            onItemClicked = {
                onEvent(CarSearchViewModel.CarSearchEvent.CarSelected(it))
            }
        )

        uiState.searchError?.let { error ->
            val message =
                when (error) {
                    NetworkError.NETWORK -> stringResource(Res.string.search_network_error)
                    NetworkError.REQUEST_REJECTED -> stringResource(Res.string.search_request_error)
                    NetworkError.SERVER -> stringResource(Res.string.search_server_error)
                    NetworkError.INVALID_RESPONSE ->
                        stringResource(Res.string.search_invalid_response_error)
                }
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
        }

        uiState.selectedCarGroup?.let { carGroup ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(carGroup.value) { car ->
                    CarDetails(
                        car = car,
                        carDetailsMode = CarDetailsMode.Selectable(
                            onSelected = {
                                onEvent(CarSearchViewModel.CarSearchEvent.CarEntrySelected(car.etxCode))
                            }
                        ),
                    )
                }
            }
        }

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
