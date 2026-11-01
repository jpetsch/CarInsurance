package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.driver_additional_postal_code
import carinsurance.shared.generated.resources.driver_company
import carinsurance.shared.generated.resources.driver_company_name_label
import carinsurance.shared.generated.resources.driver_city_lookup_error
import carinsurance.shared.generated.resources.driver_damage_animal_collision
import carinsurance.shared.generated.resources.driver_damage_alpine_hazards
import carinsurance.shared.generated.resources.driver_damage_fault
import carinsurance.shared.generated.resources.driver_damage_glass
import carinsurance.shared.generated.resources.driver_damage_in_last_three_years
import carinsurance.shared.generated.resources.driver_damage_assistance
import carinsurance.shared.generated.resources.driver_damage_elemental
import carinsurance.shared.generated.resources.driver_damage_fire
import carinsurance.shared.generated.resources.driver_damage_hail
import carinsurance.shared.generated.resources.driver_damage_liability_both
import carinsurance.shared.generated.resources.driver_damage_liability_material
import carinsurance.shared.generated.resources.driver_damage_liability_personal
import carinsurance.shared.generated.resources.driver_damage_marten
import carinsurance.shared.generated.resources.driver_damage_parking
import carinsurance.shared.generated.resources.driver_damage_passenger
import carinsurance.shared.generated.resources.driver_damage_own_vehicle_collision
import carinsurance.shared.generated.resources.driver_damage_sum
import carinsurance.shared.generated.resources.driver_damage_theft_effects
import carinsurance.shared.generated.resources.driver_damage_theft_vehicle
import carinsurance.shared.generated.resources.driver_damage_type
import carinsurance.shared.generated.resources.driver_damage_year
import carinsurance.shared.generated.resources.driver_damage_vandalism
import carinsurance.shared.generated.resources.driver_date_of_birth
import carinsurance.shared.generated.resources.driver_date_of_birth_hint
import carinsurance.shared.generated.resources.driver_driving_test_year
import carinsurance.shared.generated.resources.driver_fault_no_insurance_payment
import carinsurance.shared.generated.resources.driver_fault_no_self_payment
import carinsurance.shared.generated.resources.driver_fault_with_bonus_protection
import carinsurance.shared.generated.resources.driver_fault_yes_insurance_payment
import carinsurance.shared.generated.resources.driver_fault_yes_self_payment
import carinsurance.shared.generated.resources.driver_first_name_label
import carinsurance.shared.generated.resources.driver_gender
import carinsurance.shared.generated.resources.driver_gender_female
import carinsurance.shared.generated.resources.driver_gender_male
import carinsurance.shared.generated.resources.driver_insurance_start_date
import carinsurance.shared.generated.resources.driver_leased
import carinsurance.shared.generated.resources.driver_main_driver
import carinsurance.shared.generated.resources.driver_me
import carinsurance.shared.generated.resources.driver_nationality
import carinsurance.shared.generated.resources.driver_no
import carinsurance.shared.generated.resources.driver_other_person
import carinsurance.shared.generated.resources.driver_option_unknown
import carinsurance.shared.generated.resources.driver_payment_interval
import carinsurance.shared.generated.resources.driver_payment_interval_annual
import carinsurance.shared.generated.resources.driver_payment_interval_monthly
import carinsurance.shared.generated.resources.driver_payment_interval_semiannual
import carinsurance.shared.generated.resources.driver_postal_code_label
import carinsurance.shared.generated.resources.driver_registration_owner
import carinsurance.shared.generated.resources.driver_self
import carinsurance.shared.generated.resources.driver_usage
import carinsurance.shared.generated.resources.driver_usage_business
import carinsurance.shared.generated.resources.driver_usage_commercial_goods
import carinsurance.shared.generated.resources.driver_usage_commercial_person
import carinsurance.shared.generated.resources.driver_usage_occasional_business
import carinsurance.shared.generated.resources.driver_usage_private
import carinsurance.shared.generated.resources.driver_usage_private_and_travel
import carinsurance.shared.generated.resources.driver_usage_rental
import carinsurance.shared.generated.resources.driver_usage_rental_platforms
import carinsurance.shared.generated.resources.driver_when_should_it_start
import carinsurance.shared.generated.resources.driver_yes
import com.jpapplications.carinsurance.domain.model.DamageFault
import com.jpapplications.carinsurance.domain.model.DamageType
import com.jpapplications.carinsurance.domain.model.Gender
import com.jpapplications.carinsurance.domain.model.VehicleUsage
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
    val localizedUsageOptions = usageOptions.map { it to it.toLabel() }

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
        suffix = {
            uiState.postalCodeCity?.let { Text(it.name) }
            if (uiState.isPostalCodeCityLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            }
        },
        supportingText = {
            if (uiState.postalCodeCityError != null) {
                Text(
                    text = stringResource(Res.string.driver_city_lookup_error),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )

    Spacer(modifier = Modifier.height(16.dp))
    SelectionDropdown(
        value = localizedUsageOptions.firstOrNull { it.first == uiState.vehicleUsage }?.second.orEmpty(),
        options = localizedUsageOptions.map { it.second },
        label = stringResource(Res.string.driver_usage),
        onOptionSelected = {
            localizedUsageOptions.firstOrNull { option -> option.second == it }?.let { option ->
                onEvent(
                    DriverInformationViewModel.DriverInformationEvent.VehicleUsageChanged(
                        option.first,
                    ),
                )
            }
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
    val genderOptions =
        configuration?.gender.orEmpty().map { it to it.toLabel() }
    SelectionDropdown(
        value = genderOptions.firstOrNull { it.first == uiState.gender }?.second.orEmpty(),
        options = genderOptions.map { it.second },
        label = stringResource(Res.string.driver_gender),
        onOptionSelected = {
            genderOptions.firstOrNull { option -> option.second == it }?.let { option ->
                onEvent(DriverInformationViewModel.DriverInformationEvent.GenderChanged(option.first))
            }
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
        suffix = {
            uiState.additionalDriverCity?.let { Text(it.name) }
            if (uiState.isAdditionalPostalCodeCityLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            }
        },
        supportingText = {
            if (uiState.additionalPostalCodeCityError != null) {
                Text(
                    text = stringResource(Res.string.driver_city_lookup_error),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
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
        val damageTypeOptions =
            configuration?.schadensfallTyp.orEmpty().map { it to it.toLabel() }
        val damageFaultOptions =
            configuration?.schadensfallSchuld.orEmpty().map { it to it.toLabel() }
        Spacer(modifier = Modifier.height(8.dp))
        SelectionDropdown(
            value = damageTypeOptions.firstOrNull { it.first == uiState.damageType }?.second.orEmpty(),
            options = damageTypeOptions.map { it.second },
            label = stringResource(Res.string.driver_damage_type),
            onOptionSelected = {
                damageTypeOptions.firstOrNull { option -> option.second == it }?.let { option ->
                    onEvent(
                        DriverInformationViewModel.DriverInformationEvent.DamageTypeChanged(
                            option.first,
                        ),
                    )
                }
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
            value = damageFaultOptions.firstOrNull { it.first == uiState.damageFault }?.second.orEmpty(),
            options = damageFaultOptions.map { it.second },
            label = stringResource(Res.string.driver_damage_fault),
            onOptionSelected = {
                damageFaultOptions.firstOrNull { option -> option.second == it }?.let { option ->
                    onEvent(
                        DriverInformationViewModel.DriverInformationEvent.DamageFaultChanged(
                            option.first,
                        ),
                    )
                }
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
    val paymentIntervals =
        listOf(
            PaymentInterval.ANNUAL to stringResource(Res.string.driver_payment_interval_annual),
            PaymentInterval.SEMIANNUAL to
                stringResource(Res.string.driver_payment_interval_semiannual),
            PaymentInterval.MONTHLY to stringResource(Res.string.driver_payment_interval_monthly),
        )
    SelectionDropdown(
        value = paymentIntervals.firstOrNull { it.first == uiState.paymentInterval }?.second.orEmpty(),
        options = paymentIntervals.map { it.second },
        label = stringResource(Res.string.driver_payment_interval),
        onOptionSelected = {
            val interval = paymentIntervals.first { option -> option.second == it }.first
            onEvent(DriverInformationViewModel.DriverInformationEvent.PaymentIntervalChanged(interval))
        },
    )
}

@Composable
private fun Question(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun Gender.toLabel(): String =
    when (this) {
        Gender.Known.MALE -> stringResource(Res.string.driver_gender_male)
        Gender.Known.FEMALE -> stringResource(Res.string.driver_gender_female)
        is Gender.Unknown -> toUnknownOptionLabel(apiCode)
    }

@Composable
private fun VehicleUsage.toLabel(): String =
    when (this) {
        VehicleUsage.Known.PRIVATE -> stringResource(Res.string.driver_usage_private)
        VehicleUsage.Known.PRIVATE_AND_TRAVEL ->
            stringResource(Res.string.driver_usage_private_and_travel)
        VehicleUsage.Known.OCCASIONAL_BUSINESS ->
            stringResource(Res.string.driver_usage_occasional_business)
        VehicleUsage.Known.BUSINESS -> stringResource(Res.string.driver_usage_business)
        VehicleUsage.Known.COMMERCIAL_PERSON ->
            stringResource(Res.string.driver_usage_commercial_person)
        VehicleUsage.Known.COMMERCIAL_GOODS ->
            stringResource(Res.string.driver_usage_commercial_goods)
        VehicleUsage.Known.RENTAL_PLATFORMS ->
            stringResource(Res.string.driver_usage_rental_platforms)
        VehicleUsage.Known.RENTAL -> stringResource(Res.string.driver_usage_rental)
        is VehicleUsage.Unknown -> toUnknownOptionLabel(apiCode)
    }

@Composable
private fun DamageType.toLabel(): String =
    when (this) {
        DamageType.LIABILITY_PERSONAL ->
            stringResource(Res.string.driver_damage_liability_personal)
        DamageType.LIABILITY_MATERIAL ->
            stringResource(Res.string.driver_damage_liability_material)
        DamageType.LIABILITY_MATERIAL_AND_PERSONAL ->
            stringResource(Res.string.driver_damage_liability_both)
        DamageType.COLLISION_MATERIAL_OWN_VEHICLE ->
            stringResource(Res.string.driver_damage_own_vehicle_collision)
        DamageType.THEFT_VEHICLE -> stringResource(Res.string.driver_damage_theft_vehicle)
        DamageType.THEFT_PERSONALEFFECTS ->
            stringResource(Res.string.driver_damage_theft_effects)
        DamageType.MARTEN -> stringResource(Res.string.driver_damage_marten)
        DamageType.VANDALISM -> stringResource(Res.string.driver_damage_vandalism)
        DamageType.ELEMENTAL -> stringResource(Res.string.driver_damage_elemental)
        DamageType.ALPINE_HAZARDS -> stringResource(Res.string.driver_damage_alpine_hazards)
        DamageType.HAIL -> stringResource(Res.string.driver_damage_hail)
        DamageType.FIRE -> stringResource(Res.string.driver_damage_fire)
        DamageType.GLAS -> stringResource(Res.string.driver_damage_glass)
        DamageType.ANIMALCOLLISION -> stringResource(Res.string.driver_damage_animal_collision)
        DamageType.PARKINGDAMAGE -> stringResource(Res.string.driver_damage_parking)
        DamageType.ASSISTANCE -> stringResource(Res.string.driver_damage_assistance)
        DamageType.PASSENGER -> stringResource(Res.string.driver_damage_passenger)
    }

@Composable
private fun DamageFault.toLabel(): String =
    when (this) {
        DamageFault.WITH_BONUSSCHUTZ ->
            stringResource(Res.string.driver_fault_with_bonus_protection)
        DamageFault.YES_SELF_PAYMENT ->
            stringResource(Res.string.driver_fault_yes_self_payment)
        DamageFault.YES_INSURANCE_PAYMENT ->
            stringResource(Res.string.driver_fault_yes_insurance_payment)
        DamageFault.NO_SELF_PAYMENT ->
            stringResource(Res.string.driver_fault_no_self_payment)
        DamageFault.NO_INSURANCE_PAYMENT ->
            stringResource(Res.string.driver_fault_no_insurance_payment)
    }

@Composable
private fun toUnknownOptionLabel(apiCode: String): String =
    stringResource(
        Res.string.driver_option_unknown,
        apiCode.replace('_', ' ').lowercase().replaceFirstChar(Char::uppercase),
    )
