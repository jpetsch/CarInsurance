package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.driver_additional_postal_code
import carinsurance.shared.generated.resources.driver_company
import carinsurance.shared.generated.resources.driver_company_name_label
import carinsurance.shared.generated.resources.driver_damage_fault
import carinsurance.shared.generated.resources.driver_damage_in_last_three_years
import carinsurance.shared.generated.resources.driver_damage_sum
import carinsurance.shared.generated.resources.driver_damage_type
import carinsurance.shared.generated.resources.driver_damage_year
import carinsurance.shared.generated.resources.driver_date_of_birth
import carinsurance.shared.generated.resources.driver_date_of_birth_hint
import carinsurance.shared.generated.resources.driver_driving_test_year
import carinsurance.shared.generated.resources.driver_first_name_label
import carinsurance.shared.generated.resources.driver_gender
import carinsurance.shared.generated.resources.driver_insurance_start_date
import carinsurance.shared.generated.resources.driver_leased
import carinsurance.shared.generated.resources.driver_main_driver
import carinsurance.shared.generated.resources.driver_me
import carinsurance.shared.generated.resources.driver_nationality
import carinsurance.shared.generated.resources.driver_no
import carinsurance.shared.generated.resources.driver_other_person
import carinsurance.shared.generated.resources.driver_payment_interval
import carinsurance.shared.generated.resources.driver_payment_interval_annual
import carinsurance.shared.generated.resources.driver_payment_interval_monthly
import carinsurance.shared.generated.resources.driver_payment_interval_semiannual
import carinsurance.shared.generated.resources.driver_postal_code_label
import carinsurance.shared.generated.resources.driver_registration_owner
import carinsurance.shared.generated.resources.driver_self
import carinsurance.shared.generated.resources.driver_usage
import carinsurance.shared.generated.resources.driver_when_should_it_start
import carinsurance.shared.generated.resources.driver_yes
import com.jpapplications.carinsurance.presentation.components.ChoiceButtons
import com.jpapplications.carinsurance.presentation.components.DateInputField
import com.jpapplications.carinsurance.presentation.components.SelectionDropdown
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun VehicleInformationSection(
    uiState: DriverInformationUiState,
    onEvent: (DriverInformationViewModel.DriverInformationEvent) -> Unit,
) {
    val configuration = uiState.offerConfiguration
    val usageOptions =
        if (uiState.isCompany == true) {
            configuration?.fahrzeugNutzungAllowedForBusiness.orEmpty()
        } else {
            configuration?.fahrzeugNutzung.orEmpty()
        }

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
    SelectionDropdown(
        value = uiState.vehicleUsage.orEmpty(),
        options = usageOptions,
        label = stringResource(Res.string.driver_usage),
        onOptionSelected = {
            onEvent(DriverInformationViewModel.DriverInformationEvent.VehicleUsageChanged(it))
        },
    )
}

@Composable
internal fun MainDriverInformationSection(
    uiState: DriverInformationUiState,
    onEvent: (DriverInformationViewModel.DriverInformationEvent) -> Unit,
) {
    val configuration = uiState.offerConfiguration

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
    SelectionDropdown(
        value = uiState.gender.orEmpty(),
        options = configuration?.gender.orEmpty(),
        label = stringResource(Res.string.driver_gender),
        onOptionSelected = {
            onEvent(DriverInformationViewModel.DriverInformationEvent.GenderChanged(it))
        },
    )

    Spacer(modifier = Modifier.height(16.dp))
    DateInputField(
        value = uiState.dateOfBirth,
        label = stringResource(Res.string.driver_date_of_birth),
        placeholder = stringResource(Res.string.driver_date_of_birth_hint),
        onValueChange = {
            onEvent(DriverInformationViewModel.DriverInformationEvent.DateOfBirthChanged(it))
        },
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
}

@Composable
internal fun DamageHistorySection(
    uiState: DriverInformationUiState,
    onEvent: (DriverInformationViewModel.DriverInformationEvent) -> Unit,
) {
    val configuration = uiState.offerConfiguration

    Spacer(modifier = Modifier.height(16.dp))
    Question(stringResource(Res.string.driver_damage_in_last_three_years))
    ChoiceButtons(
        firstLabel = stringResource(Res.string.driver_yes),
        firstSelected = uiState.hadDamageInLastThreeYears == true,
        onFirstSelected = {
            onEvent(
                DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(true),
            )
        },
        secondLabel = stringResource(Res.string.driver_no),
        secondSelected = uiState.hadDamageInLastThreeYears == false,
        onSecondSelected = {
            onEvent(
                DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(false),
            )
        },
    )

    if (uiState.hadDamageInLastThreeYears == true) {
        Spacer(modifier = Modifier.height(8.dp))
        SelectionDropdown(
            value = uiState.damageType.orEmpty(),
            options = configuration?.schadensfallTyp.orEmpty(),
            label = stringResource(Res.string.driver_damage_type),
            onOptionSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.DamageTypeChanged(it))
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
        SelectionDropdown(
            value = uiState.damageYear?.toString().orEmpty(),
            options = configuration?.schadenfallYear.orEmpty().map(Int::toString),
            label = stringResource(Res.string.driver_damage_year),
            onOptionSelected = {
                it.toIntOrNull()?.let { year ->
                    onEvent(DriverInformationViewModel.DriverInformationEvent.DamageYearChanged(year))
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
        SelectionDropdown(
            value = uiState.damageFault.orEmpty(),
            options = configuration?.schadensfallSchuld.orEmpty(),
            label = stringResource(Res.string.driver_damage_fault),
            onOptionSelected = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.DamageFaultChanged(it))
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.damageSum,
            onValueChange = {
                onEvent(DriverInformationViewModel.DriverInformationEvent.DamageSumChanged(it))
            },
            label = { Text(stringResource(Res.string.driver_damage_sum)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
        )
    }
}

@Composable
internal fun InsuranceStartSection(
    uiState: DriverInformationUiState,
    onEvent: (DriverInformationViewModel.DriverInformationEvent) -> Unit,
) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = stringResource(Res.string.driver_when_should_it_start),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(8.dp))
    DateInputField(
        value = uiState.insuranceStartDate,
        label = stringResource(Res.string.driver_insurance_start_date),
        placeholder = stringResource(Res.string.driver_date_of_birth_hint),
        onValueChange = {
            onEvent(DriverInformationViewModel.DriverInformationEvent.InsuranceStartDateChanged(it))
        },
    )

    Spacer(modifier = Modifier.height(8.dp))
    SelectionDropdown(
        value = uiState.paymentInterval.orEmpty(),
        options =
            listOf(
                stringResource(Res.string.driver_payment_interval_annual),
                stringResource(Res.string.driver_payment_interval_semiannual),
                stringResource(Res.string.driver_payment_interval_monthly),
            ),
        label = stringResource(Res.string.driver_payment_interval),
        onOptionSelected = {
            onEvent(DriverInformationViewModel.DriverInformationEvent.PaymentIntervalChanged(it))
        },
    )
}

@Composable
private fun Question(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
}
