package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class DriverInformationScreenTest {
    @Test
    fun showsFormSectionsAndRequestsRecommendation() = runComposeUiTest {
        var event: DriverInformationViewModel.DriverInformationEvent? = null
        setContent {
            MaterialTheme {
                DriverInformationScreen(
                    uiState = DriverInformationUiState(),
                    onEvent = { event = it },
                )
            }
        }

        onNodeWithText("Dein Fahrzeug").assertIsDisplayed()
        onNodeWithText("Ist das Auto geleast?").assertIsDisplayed()
        onNodeWithText("Wer fährt am meisten?").assertIsDisplayed()
        onNodeWithText("Hatte der Fahrer in den letzten drei Jahren einen Schadenfall?")
            .performScrollTo()
            .assertIsDisplayed()
        onNodeWithText("Wann soll es losgehen?").performScrollTo().assertIsDisplayed()

        onNodeWithText("Unsere Empfehlung ansehen").performScrollTo().performClick()

        assertEquals(DriverInformationViewModel.DriverInformationEvent.RecommendationRequested, event)
    }

    @Test
    fun displaysRecommendationInputValidation() = runComposeUiTest {
        setContent {
            MaterialTheme {
                DriverInformationScreen(
                    uiState = DriverInformationUiState(hasRecommendationInputError = true),
                    onEvent = {},
                )
            }
        }

        onNodeWithText(
            "Bitte vervollständige alle erforderlichen Angaben, bevor du die Empfehlung anforderst.",
        ).performScrollTo().assertIsDisplayed()
    }
}
