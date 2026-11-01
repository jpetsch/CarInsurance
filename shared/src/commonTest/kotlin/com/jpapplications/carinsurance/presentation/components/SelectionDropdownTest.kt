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
class SelectionDropdownTest {
    @Test
    fun opensOptionsAndReportsTheSelectedValue() = runComposeUiTest {
        var selection = ""
        setContent {
            MaterialTheme {
                SelectionDropdown(
                    value = "",
                    options = listOf("Privat", "Geschäftlich"),
                    label = "Nutzung",
                    onOptionSelected = { selection = it },
                )
            }
        }

        onNodeWithText("Nutzung").performClick()
        onNodeWithText("Privat").assertIsDisplayed().performClick()

        assertEquals("Privat", selection)
    }

    @Test
    fun rendersDropdownWhenNoOptionsAreAvailable() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SelectionDropdown(
                    value = "",
                    options = emptyList(),
                    label = "Geschlecht",
                    onOptionSelected = {},
                )
            }
        }

        onNodeWithText("Geschlecht").assertIsDisplayed()
    }
}
