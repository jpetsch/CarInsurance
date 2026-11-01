package com.jpapplications.carinsurance.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class DateInputFieldTest {
    @Test
    fun formatsTypedDateAndReportsTheFormattedValue() = runComposeUiTest {
        var changedValue = ""
        setContent {
            MaterialTheme {
                DateInputField(
                    value = "",
                    label = "Geburtstag",
                    placeholder = "TT.MM.JJJJ",
                    onValueChange = { changedValue = it },
                )
            }
        }

        onNodeWithText("Geburtstag").assertIsDisplayed().performTextInput("23081990")

        assertEquals("23.08.1990", changedValue)
        onNodeWithText("23.08.1990").assertIsDisplayed()
    }

    @Test
    fun formatsMonthCorrectlyWhenDigitsAreEnteredOneAtATime() = runComposeUiTest {
        setContent {
            MaterialTheme {
                DateInputField(
                    value = "",
                    label = "Geburtstag",
                    placeholder = "TT.MM.JJJJ",
                    onValueChange = {},
                )
            }
        }

        onNodeWithText("Geburtstag").performTextInput("23")
        onNodeWithText("Geburtstag").performTextInput("0")
        onNodeWithText("Geburtstag").performTextInput("8")

        onNodeWithText("23.08").assertIsDisplayed()
    }
}
