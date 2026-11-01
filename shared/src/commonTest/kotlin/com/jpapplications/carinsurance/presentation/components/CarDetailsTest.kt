package com.jpapplications.carinsurance.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.jpapplications.carinsurance.domain.model.Car
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CarDetailsTest {
    @Test
    fun showsCarInformationAndReportsSelection() = runComposeUiTest {
        var selectedCar: Car? = null
        setContent {
            MaterialTheme {
                CarDetails(
                    car = testCar,
                    carDetailsMode = CarDetailsMode.Selectable { selectedCar = it },
                )
            }
        }

        onNodeWithText("Golf GTI").assertIsDisplayed()
        onNodeWithText("150 kW / 204 PS").assertIsDisplayed()
        onNode(hasClickAction()).performClick()

        assertEquals(testCar, selectedCar)
    }

    private companion object {
        val testCar =
            Car(
                matriculationDate = 2020,
                constructionStartYear = 2020,
                catalogPrice = 40_000,
                etxCode = 123,
                description = "Golf GTI",
                capacity = 1_984,
                model = "Golf",
                doors = 5,
                seats = 5,
                emptyLoad = 1_400,
                horsePower = 204,
                enginePowerInKw = 150,
                vehicleType = "CAR",
                vehicleClass = 1,
                constructionType = "HATCHBACK",
                fuel = "PETROL",
                brand = "Volkswagen",
                eMobility = false,
            )
    }
}
