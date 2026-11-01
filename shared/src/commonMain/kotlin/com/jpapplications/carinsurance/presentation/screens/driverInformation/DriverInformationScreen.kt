package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.driver_accessories_value
import carinsurance.shared.generated.resources.driver_accessories_value_minimum
import carinsurance.shared.generated.resources.driver_configuration_load_error
import carinsurance.shared.generated.resources.driver_recommendation_input_error
import carinsurance.shared.generated.resources.driver_recommendation_error
import carinsurance.shared.generated.resources.driver_recommendation_premium
import carinsurance.shared.generated.resources.driver_vehicle_title
import carinsurance.shared.generated.resources.driver_vehicle_load_error
import carinsurance.shared.generated.resources.driver_view_recommendation
import com.jpapplications.carinsurance.presentation.components.CarDetails
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToLong

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(Res.string.driver_vehicle_title),
            style = MaterialTheme.typography.titleLarge,
        )

        if (uiState.vehicleModelError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.driver_vehicle_load_error),
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        uiState.car?.let { car ->
            CarDetails(car = car)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.newValueAccessories,
                onValueChange = {
                    onEvent(
                        DriverInformationViewModel.DriverInformationEvent.NewValueAccessoriesChanged(it),
                    )
                },
                label = { Text(stringResource(Res.string.driver_accessories_value)) },
                supportingText = {
                    Text(
                        stringResource(
                            Res.string.driver_accessories_value_minimum,
                            uiState.newValueAccessoriesMinimum,
                        ),
                    )
                },
                isError =
                    uiState.newValueAccessories.toIntOrNull()?.let {
                        it < uiState.newValueAccessoriesMinimum
                    } ?: true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            onEvent(
                                DriverInformationViewModel.DriverInformationEvent
                                    .NewValueAccessoriesFocusChanged(focusState.isFocused),
                            )
                        },
                singleLine = true,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        VehicleInformationSection(uiState, onEvent)
        MainDriverInformationSection(uiState, onEvent)
        DamageHistorySection(uiState, onEvent)
        InsuranceStartSection(uiState, onEvent)

        if (uiState.offerConfigurationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.driver_configuration_load_error),
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.RecommendationRequested)
            },
            enabled = !uiState.isRecommendationLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isRecommendationLoading) {
                CircularProgressIndicator()
            } else {
                Text(stringResource(Res.string.driver_view_recommendation))
            }
        }

        if (uiState.hasRecommendationInputError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.driver_recommendation_input_error),
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (uiState.recommendationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.driver_recommendation_error),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }

}
