package com.jpapplications.carinsurance.presentation.screens.carSearch

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import com.jpapplications.carinsurance.domain.model.NetworkError
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CarSearchScreenTest {
    @Test
    fun showsYearValidationAndSearchFailureMessages() = runComposeUiTest {
        setContent {
            MaterialTheme {
                CarSearchScreen(
                    uiState =
                        CarSearchUiState(
                            year = "1974",
                            hasYearError = true,
                            searchError = NetworkError.NETWORK,
                        ),
                    onEvent = {},
                )
            }
        }

        onNodeWithText("Erste Inverkehrssetzung (Jahr)").assertIsDisplayed()
        onNodeWithText("Bitte gib ein gültiges Jahr ein.").assertIsDisplayed()
        onNodeWithText("Netzwerkfehler. Bitte überprüfe deine Verbindung.").assertIsDisplayed()
    }

    @Test
    fun typingSearchTermEmitsSearchEvent() = runComposeUiTest {
        var event: CarSearchViewModel.CarSearchEvent? = null
        setContent {
            MaterialTheme {
                CarSearchScreen(
                    uiState = CarSearchUiState(),
                    onEvent = { event = it },
                )
            }
        }

        onNodeWithText("Fahrzeug suchen").performTextInput("Golf")

        kotlin.test.assertEquals(
            CarSearchViewModel.CarSearchEvent.SearchTermChanged("Golf"),
            event,
        )
    }
}
