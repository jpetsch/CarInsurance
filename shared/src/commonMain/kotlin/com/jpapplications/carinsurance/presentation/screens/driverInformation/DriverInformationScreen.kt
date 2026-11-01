package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.driver_company
import carinsurance.shared.generated.resources.driver_company_name_label
import carinsurance.shared.generated.resources.driver_configuration_load_error
import carinsurance.shared.generated.resources.driver_additional_postal_code
import carinsurance.shared.generated.resources.driver_date_of_birth
import carinsurance.shared.generated.resources.driver_date_of_birth_hint
import carinsurance.shared.generated.resources.driver_driving_test_year
import carinsurance.shared.generated.resources.driver_first_name_label
import carinsurance.shared.generated.resources.driver_gender
import carinsurance.shared.generated.resources.driver_leased
import carinsurance.shared.generated.resources.driver_main_driver
import carinsurance.shared.generated.resources.driver_me
import carinsurance.shared.generated.resources.driver_no
import carinsurance.shared.generated.resources.driver_nationality
import carinsurance.shared.generated.resources.driver_other_person
import carinsurance.shared.generated.resources.driver_postal_code_label
import carinsurance.shared.generated.resources.driver_registration_owner
import carinsurance.shared.generated.resources.driver_self
import carinsurance.shared.generated.resources.driver_usage
import carinsurance.shared.generated.resources.driver_vehicle_title
import carinsurance.shared.generated.resources.driver_yes
import com.jpapplications.carinsurance.presentation.components.CarDetails
import org.jetbrains.compose.resources.stringResource

@Composable
fun DriverInformationScreen(
    uiState: DriverInformationUiState,
    onEvent: (event: DriverInformationViewModel.DriverInformationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dateOfBirthField by remember {
        mutableStateOf(
            TextFieldValue(
                text = uiState.dateOfBirth,
                selection = TextRange(uiState.dateOfBirth.length),
            ),
        )
    }

    LaunchedEffect(uiState.dateOfBirth) {
        if (dateOfBirthField.text != uiState.dateOfBirth) {
            dateOfBirthField =
                TextFieldValue(
                    text = uiState.dateOfBirth,
                    selection = TextRange(uiState.dateOfBirth.length),
                )
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(Res.string.driver_vehicle_title),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        uiState.car?.let { car ->
            CarDetails(
                car = car,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Question(stringResource(Res.string.driver_leased))
        ChoiceButtons(
            firstLabel = stringResource(Res.string.driver_yes),
            firstSelected = uiState.isLeased == true,
            onFirstSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.LeasingChanged(true))
            },
            secondLabel = stringResource(Res.string.driver_no),
            secondSelected = uiState.isLeased == false,
            onSecondSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.LeasingChanged(false))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Question(stringResource(Res.string.driver_registration_owner))
        ChoiceButtons(
            firstLabel = stringResource(Res.string.driver_me),
            firstSelected = uiState.isCompany == false,
            onFirstSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.RegistrationOwnerChanged(false))
            },
            secondLabel = stringResource(Res.string.driver_company),
            secondSelected = uiState.isCompany == true,
            onSecondSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.RegistrationOwnerChanged(true))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.firstName,
            onValueChange = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.FirstNameChanged(it))
            },
            label = {
                Text(
                    stringResource(
                        if (uiState.isCompany == true) {
                            Res.string.driver_company_name_label
                        } else {
                            Res.string.driver_first_name_label
                        },
                    ),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.postalCode,
            onValueChange = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.PostalCodeChanged(it))
            },
            label = { Text(stringResource(Res.string.driver_postal_code_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        val configuration = uiState.offerConfiguration
        val usageOptions =
            if (uiState.isCompany == true) {
                configuration?.fahrzeugNutzungAllowedForBusiness.orEmpty()
            } else {
                configuration?.fahrzeugNutzung.orEmpty()
            }
        SelectionDropdown(
            value = uiState.vehicleUsage.orEmpty(),
            options = usageOptions,
            label = stringResource(Res.string.driver_usage),
            onOptionSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.VehicleUsageChanged(it))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Question(stringResource(Res.string.driver_main_driver))
        ChoiceButtons(
            firstLabel = stringResource(Res.string.driver_self),
            firstSelected = uiState.isMainDriverSelf == true,
            onFirstSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.MainDriverChanged(true))
            },
            secondLabel = stringResource(Res.string.driver_other_person),
            secondSelected = uiState.isMainDriverSelf == false,
            onSecondSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.MainDriverChanged(false))
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        SelectionDropdown(
            value = uiState.gender.orEmpty(),
            options = configuration?.gender.orEmpty(),
            label = stringResource(Res.string.driver_gender),
            onOptionSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.GenderChanged(it))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = dateOfBirthField,
            onValueChange = { input ->
                val formatted = formatDateOfBirthInput(input)
                dateOfBirthField = formatted
                onEvent(
                    DriverInformationViewModel.DriverInformationEvent.DateOfBirthChanged(
                        formatted.text,
                    ),
                )
            },
            label = { Text(stringResource(Res.string.driver_date_of_birth)) },
            placeholder = { Text(stringResource(Res.string.driver_date_of_birth_hint)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.nationality,
            onValueChange = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.NationalityChanged(it))
            },
            label = { Text(stringResource(Res.string.driver_nationality)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.additionalDriverPostalCode,
            onValueChange = {
                onEvent(
                    DriverInformationViewModel.DriverInformationEvent.AdditionalDriverPostalCodeChanged(it),
                )
            },
            enabled = uiState.isMainDriverSelf == false,
            label = { Text(stringResource(Res.string.driver_additional_postal_code)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.drivingTestYear,
            onValueChange = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.DrivingTestYearChanged(it))
            },
            label = { Text(stringResource(Res.string.driver_driving_test_year)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        if (uiState.offerConfigurationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.driver_configuration_load_error),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

private fun formatDateOfBirthInput(input: TextFieldValue): TextFieldValue {
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

@Composable
private fun Question(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ChoiceButtons(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionDropdown(
    value: String,
    options: List<String>,
    label: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && options.isNotEmpty() },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = options.isNotEmpty(),
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier =
                Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}