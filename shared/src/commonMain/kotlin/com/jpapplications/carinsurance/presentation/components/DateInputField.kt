package com.jpapplications.carinsurance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun DateInputField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            ),
        )
    }

    LaunchedEffect(value) {
        if (fieldValue.text != value) {
            fieldValue = TextFieldValue(value, selection = TextRange(value.length))
        }
    }

    OutlinedTextField(
        value = fieldValue,
        onValueChange = { input ->
            val formatted = formatDateInput(input)
            fieldValue = formatted
            onValueChange(formatted.text)
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

private fun formatDateInput(input: TextFieldValue): TextFieldValue {
    val digitsBeforeCursor = input.text.take(input.selection.start).count(Char::isDigit)
    val digits = input.text.filter(Char::isDigit).take(8)
    val formatted =
        buildString {
            digits.forEachIndexed { index, digit ->
                if (index == 2 || index == 4) append('.')
                append(digit)
            }
        }
    val separatorCount =
        (if (digitsBeforeCursor > 2) 1 else 0) +
            (if (digitsBeforeCursor > 4) 1 else 0)
    val cursor = (digitsBeforeCursor + separatorCount).coerceAtMost(formatted.length)

    return TextFieldValue(
        text = formatted,
        selection = TextRange(cursor),
    )
}
