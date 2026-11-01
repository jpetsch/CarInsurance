package com.jpapplications.carinsurance.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceButtons(
    firstLabel: String,
    firstSelected: Boolean,
    onFirstSelected: () -> Unit,
    secondLabel: String,
    secondSelected: Boolean,
    onSecondSelected: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onFirstSelected,
            modifier = Modifier.weight(1f),
            colors = choiceButtonColors(firstSelected),
        ) {
            Text(firstLabel)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSecondSelected,
            modifier = Modifier.weight(1f),
            colors = choiceButtonColors(secondSelected),
        ) {
            Text(secondLabel)
        }
    }
}

@Composable
private fun choiceButtonColors(selected: Boolean) =
    ButtonDefaults.buttonColors(
        containerColor =
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor =
            if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
    )
