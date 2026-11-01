package com.jpapplications.carinsurance.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.car_catalog_price
import carinsurance.shared.generated.resources.car_construction_type
import carinsurance.shared.generated.resources.car_doors
import carinsurance.shared.generated.resources.car_empty_load
import carinsurance.shared.generated.resources.car_empty_load_value
import carinsurance.shared.generated.resources.car_fuel
import carinsurance.shared.generated.resources.car_power
import carinsurance.shared.generated.resources.car_power_value
import com.jpapplications.carinsurance.domain.model.Car
import org.jetbrains.compose.resources.stringResource

@Composable
fun CarDetails(
    car: Car,
    carDetailsMode: CarDetailsMode = CarDetailsMode.NonSelectable,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(
        when(carDetailsMode) {
                    is CarDetailsMode.Selectable -> {
                        Modifier.clickable(
                            onClick = { carDetailsMode.onSelected(car) },
                        )
                    }
                    is CarDetailsMode.NonSelectable -> Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            if (carDetailsMode is CarDetailsMode.Selectable) {
                RadioButton(
                    selected = false,
                    onClick = null,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = car.description,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CarDetailRow(
                        firstLabel = stringResource(Res.string.car_construction_type),
                        firstValue = car.constructionType.toDisplayText(),
                        secondLabel = stringResource(Res.string.car_fuel),
                        secondValue = car.fuel.toDisplayText(),
                    )
                    CarDetailRow(
                        firstLabel = stringResource(Res.string.car_empty_load),
                        firstValue = stringResource(Res.string.car_empty_load_value, car.emptyLoad),
                        secondLabel = stringResource(Res.string.car_doors),
                        secondValue = car.doors.toString(),
                    )
                    CarDetailRow(
                        firstLabel = stringResource(Res.string.car_power),
                        firstValue = stringResource(
                            Res.string.car_power_value,
                            car.enginePowerInKw,
                            car.horsePower,
                        ),
                        secondLabel = stringResource(Res.string.car_catalog_price),
                        secondValue = car.catalogPrice.toString(),
                    )
                }
            }
        }
    }
}

@Composable
private fun CarDetailRow(
    firstLabel: String,
    firstValue: String,
    secondLabel: String,
    secondValue: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CarDetailItem(firstLabel, firstValue, Modifier.weight(1f))
        CarDetailItem(secondLabel, secondValue, Modifier.weight(1f))
    }
}

@Composable
private fun CarDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun String.toDisplayText(): String =
    lowercase()
        .split('_')
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }

sealed interface CarDetailsMode {
    data class Selectable(
        val onSelected: (car: Car) -> Unit,
    ) : CarDetailsMode

    data object NonSelectable : CarDetailsMode
}

@Preview
@Composable
private fun CarDetailsNonSelectablePreview() {
    CarDetails(
        car = Car(
            matriculationDate = 2020,
            constructionStartYear = 2020,
            catalogPrice = 63200,
            etxCode = 102226758,
            description = "Golf 2.0 TSI R DSG 4Motion",
            capacity = 1998,
            model = "GOLF VIII BENZIN 5 Türer Modell 2020-",
            doors = 5,
            seats = 5,
            emptyLoad = 1569,
            horsePower = 320,
            enginePowerInKw = 235,
            vehicleType = "PKW",
            vehicleClass = 30003,
            constructionType = "LIMOUSINE_SEDAN",
            fuel = "BENZIN_BLEIFREI",
            brand = "VW",
            eMobility = false,
        ),
        carDetailsMode = CarDetailsMode.NonSelectable,
    )
}

@Preview
@Composable
private fun CarDetailsSelectablePreview() {
    CarDetails(
        car = Car(
            matriculationDate = 2020,
            constructionStartYear = 2020,
            catalogPrice = 63200,
            etxCode = 102226758,
            description = "Golf 2.0 TSI R DSG 4Motion",
            capacity = 1998,
            model = "GOLF VIII BENZIN 5 Türer Modell 2020-",
            doors = 5,
            seats = 5,
            emptyLoad = 1569,
            horsePower = 320,
            enginePowerInKw = 235,
            vehicleType = "PKW",
            vehicleClass = 30003,
            constructionType = "LIMOUSINE_SEDAN",
            fuel = "BENZIN_BLEIFREI",
            brand = "VW",
            eMobility = false,
        ),
        carDetailsMode = CarDetailsMode.Selectable(
            onSelected = {}
        ),
    )
}