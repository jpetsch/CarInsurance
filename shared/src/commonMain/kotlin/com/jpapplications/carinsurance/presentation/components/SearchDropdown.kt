package com.jpapplications.carinsurance.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.jpapplications.carinsurance.domain.model.Car
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.search_term_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDropdown(
    searchTerm: String,
    cars: List<Car>,
    onSearchTermChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expanded = cars.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = onSearchTermChanged,
            label = { Text(stringResource(Res.string.search_term_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {},
        ) {
            cars.forEach { car ->
                Text(text = "${car.brand} ${car.model}")
            }
        }
    }
}
