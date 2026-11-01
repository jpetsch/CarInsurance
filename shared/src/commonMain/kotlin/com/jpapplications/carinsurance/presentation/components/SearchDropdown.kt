package com.jpapplications.carinsurance.presentation.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jpapplications.carinsurance.domain.model.Car
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.search_term_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDropdown(
    searchTerm: String,
    carGroups: Map<String, List<Car>>,
    onSearchTermChanged: (String) -> Unit,
    onItemClicked: (Map.Entry<String, List<Car>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(carGroups.isNotEmpty()) }

    LaunchedEffect(carGroups) {
        expanded = carGroups.isNotEmpty()
    }

    val dismissDropdown = { expanded = false }

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
            onDismissRequest = dismissDropdown,
            modifier = Modifier.heightIn(max = 250.dp),
        ) {
            carGroups.entries.forEachIndexed { index, carGroup ->
                DropdownMenuItem(
                    text = { Text(text = carGroup.key) },
                    onClick = {
                        onItemClicked(carGroup)
                        dismissDropdown()
                    },
                )
                if (index < carGroups.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}
