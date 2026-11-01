package com.jpapplications.carinsurance.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ChoiceButtonsTest {
    @Test
    fun displaysBothChoicesAndCallsTheSelectedAction() = runComposeUiTest {
        var selected = ""
        setContent {
            MaterialTheme {
                ChoiceButtons(
                    firstLabel = "Ja",
                    firstSelected = false,
                    onFirstSelected = { selected = "yes" },
                    secondLabel = "Nein",
                    secondSelected = true,
                    onSecondSelected = { selected = "no" },
                )
            }
        }

        onNodeWithText("Ja").assertIsDisplayed().performClick()
        assertEquals("yes", selected)

        onNodeWithText("Nein").assertIsDisplayed().performClick()
        assertEquals("no", selected)
    }
}
