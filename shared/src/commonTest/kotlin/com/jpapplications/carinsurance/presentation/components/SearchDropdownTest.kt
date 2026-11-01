package com.jpapplications.carinsurance.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.jpapplications.carinsurance.domain.model.Car
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SearchDropdownTest {
    @Test
    fun displaysGroupsAndReportsTheSelectedGroup() = runComposeUiTest {
        var selectedGroup: Map.Entry<String, List<Car>>? = null
        setContent {
            MaterialTheme {
                SearchDropdown(
                    searchTerm = "Golf",
                    carGroups = carGroups,
                    onSearchTermChanged = {},
                    onItemClicked = { selectedGroup = it },
                )
            }
        }

        onNodeWithText("Fahrzeug suchen").assertIsDisplayed()
        onNodeWithText("Volkswagen").assertIsDisplayed().performClick()

        assertEquals(carGroups.entries.single(), selectedGroup)
    }

    private companion object {
        val carGroups =
            mapOf(
                "Volkswagen" to
                    listOf(
                        Car(
                            matriculationDate = 2020,
                            constructionStartYear = 2020,
                            catalogPrice = 40_000,
                            etxCode = 123,
                            description = "Golf",
                            capacity = 1_500,
                            model = "Golf",
                            doors = 5,
                            seats = 5,
                            emptyLoad = 1_400,
                            horsePower = 150,
                            enginePowerInKw = 110,
                            vehicleType = "CAR",
                            vehicleClass = 1,
                            constructionType = "HATCHBACK",
                            fuel = "PETROL",
                            brand = "Volkswagen",
                            eMobility = false,
                        ),
                    ),
            )
    }
}
