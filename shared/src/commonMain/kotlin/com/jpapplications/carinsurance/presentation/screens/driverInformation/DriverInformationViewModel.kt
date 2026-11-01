package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult
import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfiguration
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.model.RecommendationClaim
import com.jpapplications.carinsurance.domain.model.RecommendationCity
import com.jpapplications.carinsurance.domain.model.RecommendationDriver
import com.jpapplications.carinsurance.domain.model.RecommendationVehicle
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.usecase.GetCarRecommendationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetCityByPostalCodeUseCase
import com.jpapplications.carinsurance.domain.usecase.GetOfferConfigurationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class DriverInformationViewModel(
    private val etxCode: Int,
    private val getVehicleModelUseCase: GetVehicleModelUseCase,
    private val getOfferConfigurationUseCase: GetOfferConfigurationUseCase,
    private val getCarRecommendationUseCase: GetCarRecommendationUseCase,
    private val getCityByPostalCodeUseCase: GetCityByPostalCodeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DriverInformationUiState())
    val uiState: StateFlow<DriverInformationUiState> = _uiState.asStateFlow()
    private val _showRecommendationEvent: MutableSharedFlow<CarRecommendation> = MutableSharedFlow()
    val showRecommendationEvent: SharedFlow<CarRecommendation> = _showRecommendationEvent.asSharedFlow()
    private var postalCodeLookupJob: Job? = null
    private var additionalPostalCodeLookupJob: Job? = null

    init {
        loadCarDetails()
        loadOfferConfiguration()
    }

    sealed interface DriverInformationEvent {
        data class LeasingChanged(val isLeased: Boolean) : DriverInformationEvent
        data class RegistrationOwnerChanged(val isCompany: Boolean) : DriverInformationEvent
        data class FirstNameChanged(val firstName: String) : DriverInformationEvent
        data class PostalCodeChanged(val postalCode: String) : DriverInformationEvent
        data class NewValueAccessoriesChanged(val value: String) : DriverInformationEvent
        data class NewValueAccessoriesFocusChanged(val isFocused: Boolean) : DriverInformationEvent
        data class VehicleUsageChanged(val vehicleUsage: String) : DriverInformationEvent
        data class MainDriverChanged(val isSelf: Boolean) : DriverInformationEvent
        data class GenderChanged(val gender: String) : DriverInformationEvent
        data class DateOfBirthChanged(val dateOfBirth: String) : DriverInformationEvent
        data class NationalityChanged(val nationality: String) : DriverInformationEvent
        data class AdditionalDriverPostalCodeChanged(val postalCode: String) : DriverInformationEvent
        data class DrivingTestYearChanged(val year: String) : DriverInformationEvent
        data class DamageInLastThreeYearsChanged(val hadDamage: Boolean) : DriverInformationEvent
        data class DamageTypeChanged(val damageType: String) : DriverInformationEvent
        data class DamageYearChanged(val year: Int) : DriverInformationEvent
        data class DamageFaultChanged(val fault: String) : DriverInformationEvent
        data class DamageSumChanged(val sum: String) : DriverInformationEvent
        data class InsuranceStartDateChanged(val date: String) : DriverInformationEvent
        data class PaymentIntervalChanged(val interval: PaymentInterval) : DriverInformationEvent
        data object RecommendationRequested : DriverInformationEvent
    }

    fun onEvent(event: DriverInformationEvent) {
        when (event) {
            is DriverInformationEvent.LeasingChanged ->
                _uiState.update { it.copy(isLeased = event.isLeased) }
            is DriverInformationEvent.RegistrationOwnerChanged ->
                _uiState.update {
                    it.copy(
                        isCompany = event.isCompany,
                        vehicleUsage = null,
                    )
                }
            is DriverInformationEvent.FirstNameChanged ->
                _uiState.update { it.copy(firstName = event.firstName) }
            is DriverInformationEvent.PostalCodeChanged ->
                updatePostalCode(event.postalCode, isAdditionalDriver = false)
            is DriverInformationEvent.NewValueAccessoriesChanged ->
                _uiState.update {
                    it.copy(newValueAccessories = event.value.filter(Char::isDigit).take(9))
                }
            is DriverInformationEvent.NewValueAccessoriesFocusChanged ->
                if (!event.isFocused) normalizeNewValueAccessories()
            is DriverInformationEvent.VehicleUsageChanged ->
                _uiState.update { it.copy(vehicleUsage = event.vehicleUsage) }
            is DriverInformationEvent.MainDriverChanged -> {
                _uiState.update {
                    it.copy(
                        isMainDriverSelf = event.isSelf,
                        additionalDriverPostalCode =
                            if (event.isSelf) "" else it.additionalDriverPostalCode,
                        additionalDriverCity =
                            if (event.isSelf) null else it.additionalDriverCity,
                        additionalPostalCodeCityError =
                            if (event.isSelf) null else it.additionalPostalCodeCityError,
                        isAdditionalPostalCodeCityLoading =
                            if (event.isSelf) false else it.isAdditionalPostalCodeCityLoading,
                    )
                }
                if (event.isSelf) {
                    additionalPostalCodeLookupJob?.cancel()
                    additionalPostalCodeLookupJob = null
                }
            }
            is DriverInformationEvent.GenderChanged ->
                _uiState.update { it.copy(gender = event.gender) }
            is DriverInformationEvent.DateOfBirthChanged ->
                _uiState.update { it.copy(dateOfBirth = formatDateOfBirth(event.dateOfBirth)) }
            is DriverInformationEvent.NationalityChanged ->
                _uiState.update { it.copy(nationality = event.nationality) }
            is DriverInformationEvent.AdditionalDriverPostalCodeChanged ->
                updatePostalCode(event.postalCode, isAdditionalDriver = true)
            is DriverInformationEvent.DrivingTestYearChanged ->
                _uiState.update {
                    it.copy(drivingTestYear = event.year.filter(Char::isDigit).take(4))
                }
            is DriverInformationEvent.DamageInLastThreeYearsChanged ->
                _uiState.update {
                    it.copy(
                        hadDamageInLastThreeYears = event.hadDamage,
                        damageType = if (event.hadDamage) it.damageType else null,
                        damageYear = if (event.hadDamage) it.damageYear else null,
                        damageFault = if (event.hadDamage) it.damageFault else null,
                        damageSum = if (event.hadDamage) it.damageSum else "",
                    )
                }
            is DriverInformationEvent.DamageTypeChanged ->
                _uiState.update { it.copy(damageType = event.damageType) }
            is DriverInformationEvent.DamageYearChanged ->
                _uiState.update { it.copy(damageYear = event.year) }
            is DriverInformationEvent.DamageFaultChanged ->
                _uiState.update { it.copy(damageFault = event.fault) }
            is DriverInformationEvent.DamageSumChanged ->
                _uiState.update { it.copy(damageSum = event.sum) }
            is DriverInformationEvent.InsuranceStartDateChanged ->
                _uiState.update { it.copy(insuranceStartDate = event.date) }
            is DriverInformationEvent.PaymentIntervalChanged ->
                _uiState.update { it.copy(paymentInterval = event.interval) }
            DriverInformationEvent.RecommendationRequested -> getCarRecommendation()
        }
    }

    private fun loadCarDetails() {
        viewModelScope.launch {
            when(val result = getVehicleModelUseCase(etxCode)) {
                is VehicleModelResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            car = result.car,
                            vehicleModelError = null,
                            newValueAccessoriesMinimum = result.car.newValueAccessoriesMinimum(),
                            newValueAccessories =
                                result.car.newValueAccessoriesMinimum().toString(),
                        )
                    }
                }
                is VehicleModelResult.Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(vehicleModelError = result.error)
                    }
                }
            }
        }
    }

    private fun loadOfferConfiguration() {
        viewModelScope.launch {
            when (val result = getOfferConfigurationUseCase()) {
                is OfferConfigurationResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            offerConfiguration = result.configuration,
                        )
                    }
                }
                is OfferConfigurationResult.Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(offerConfigurationError = result.error)
                    }
                }
            }
        }
    }

    private fun getCarRecommendation() {
        viewModelScope.launch {
            normalizeNewValueAccessories()
            _uiState.update {
                it.copy(
                    isRecommendationLoading = true,
                    recommendationError = null,
                    hasRecommendationInputError = false,
                )
            }

            val currentState = uiState.value
            val selectedPostalCodeLookupJob =
                when (currentState.isMainDriverSelf) {
                    true -> postalCodeLookupJob
                    false -> additionalPostalCodeLookupJob
                    null -> null
                }
            selectedPostalCodeLookupJob?.join()

            val updatedState = uiState.value
            val request = updatedState.toRecommendationRequest()

            if (request == null) {
                _uiState.update {
                    it.copy(
                        isRecommendationLoading = false,
                        hasRecommendationInputError = true,
                        recommendationError = null,
                    )
                }
                return@launch
            }

            when (val result = getCarRecommendationUseCase(request)) {
                is CarRecommendationResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isRecommendationLoading = false,
                        )
                    }

                    _showRecommendationEvent.emit(result.recommendation)
                }

                is CarRecommendationResult.Failure ->
                    _uiState.update {
                        it.copy(
                            isRecommendationLoading = false,
                            recommendationError = result.error,
                        )
                    }
            }
        }
    }

    private fun DriverInformationUiState.toRecommendationRequest(): CarRecommendationRequest? {
        val selectedCar = car ?: return null
        val selectedConfiguration = offerConfiguration ?: return null
        val selectedLeasing = isLeased ?: return null
        val selectedUsage = vehicleUsage ?: return null
        val selectedGender = gender ?: return null
        val hadDamage = hadDamageInLastThreeYears ?: return null
        val selectedCity =
            when (isMainDriverSelf) {
                true -> postalCodeCity
                false -> additionalDriverCity
                null -> return null
            } ?: return null
        val dateOfBirthIso = dateOfBirth.toIsoDate() ?: return null
        val insuranceStartIso = insuranceStartDateToIso() ?: return null
        val licenseYear = drivingTestYear.toIntOrNull() ?: return null
        val financing =
            selectedConfiguration.finanzierung.firstOrNull {
                if (selectedLeasing) {
                    it.contains("LEAS", ignoreCase = true)
                } else {
                    it.equals("SELF", ignoreCase = true)
                }
            } ?: return null
        val paymentIntervalCode = paymentInterval?.apiCode ?: return null
        val selectedHolderType =
            selectedConfiguration.halterTyp.firstOrNull {
                if (isMainDriverSelf == true) {
                    it == "HAEUFIGSTERLENKER"
                } else {
                    it != "HAEUFIGSTERLENKER"
                }
            } ?: return null
        val claims =
            if (hadDamage) {
                val type = damageType ?: return null
                val year = damageYear ?: return null
                val fault = damageFault ?: return null
                val sum = damageSum.replace(',', '.').toDoubleOrNull() ?: return null
                listOf(
                    RecommendationClaim(
                        schadensfallTyp = type,
                        schadenfallYear = year,
                        schadensfallSchuld = fault,
                        summe = sum,
                    ),
                )
            } else {
                emptyList()
            }

        return CarRecommendationRequest(
            versicherungsBeginn = insuranceStartIso,
            zahlungsintervall = paymentIntervalCode,
            halterTyp = selectedHolderType,
            fahrzeug =
                RecommendationVehicle(
                    car = selectedCar,
                    matriculationDate = selectedCar.matriculationDate,
                    newValueAccessories =
                        newValueAccessories.toIntOrNull()?.coerceAtLeast(
                            selectedCar.newValueAccessoriesMinimum(),
                        ) ?: selectedCar.newValueAccessoriesMinimum(),
                    financing = financing,
                    vehicleUsage = selectedUsage,
                ),
            fahrzeugLenker =
                RecommendationDriver(
                    geschlecht = selectedGender,
                    geburtsdatum = dateOfBirthIso,
                    nationalitaet =
                        if (nationality.equals("Schweiz", ignoreCase = true)) {
                            "CH"
                        } else {
                            nationality.trim().uppercase()
                        },
                    stadt = selectedCity,
                    jahrFahrpruefungPkw = licenseYear,
                    schadensfaelle = claims,
                ),
        )
    }

    private fun String.toIsoDate(): String? {
        if (length != 10 || this[2] != '.' || this[5] != '.') return null
        val day = substring(0, 2).toIntOrNull() ?: return null
        val month = substring(3, 5).toIntOrNull() ?: return null
        val year = substring(6, 10).toIntOrNull() ?: return null
        if (month !in 1..12) return null
        val isLeapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
        val daysInMonth =
            when (month) {
                2 -> if (isLeapYear) 29 else 28
                4, 6, 9, 11 -> 30
                else -> 31
            }
        if (day !in 1..daysInMonth) return null
        return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${
            day.toString().padStart(2, '0')
        }"
    }

    private fun insuranceStartDateOrTomorrow(): String {
        val tomorrowEpochDay = Clock.System.now().epochSeconds / SECONDS_PER_DAY + 1
        val shiftedDay = tomorrowEpochDay + DAYS_TO_UNIX_EPOCH
        val era =
            if (shiftedDay >= 0) shiftedDay / DAYS_PER_400_YEARS
            else (shiftedDay - DAYS_PER_400_YEARS + 1) / DAYS_PER_400_YEARS
        val dayOfEra = shiftedDay - era * DAYS_PER_400_YEARS
        val yearOfEra =
            (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
        var year = yearOfEra + era * 400
        val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
        val monthPrime = (5 * dayOfYear + 2) / 153
        val day = dayOfYear - (153 * monthPrime + 2) / 5 + 1
        val month = monthPrime + if (monthPrime < 10) 3 else -9
        if (month <= 2) year++
        return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${
            day.toString().padStart(2, '0')
        }"
    }

    private fun DriverInformationUiState.insuranceStartDateToIso(): String? =
        if (insuranceStartDate.isBlank()) insuranceStartDateOrTomorrow()
        else insuranceStartDate.toIsoDate()

    private fun formatDateOfBirth(value: String): String {
        val digits = value.filter(Char::isDigit).take(8)
        return buildString {
            digits.forEachIndexed { index, digit ->
                if (index == 2 || index == 4) append('.')
                append(digit)
            }
        }
    }

    private fun normalizeNewValueAccessories() {
        _uiState.update { state ->
            val minimum = state.car?.newValueAccessoriesMinimum() ?: MIN_NEW_VALUE_ACCESSORIES
            val value = state.newValueAccessories.toIntOrNull()?.coerceAtLeast(minimum) ?: minimum
            state.copy(
                newValueAccessories = value.toString(),
                newValueAccessoriesMinimum = minimum,
            )
        }
    }

    private fun Car.newValueAccessoriesMinimum(): Int {
        val tenPercentRoundedUp = ((catalogPrice.toLong() + 9) / 10).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
        return maxOf(MIN_NEW_VALUE_ACCESSORIES, tenPercentRoundedUp)
    }

    private fun updatePostalCode(postalCode: String, isAdditionalDriver: Boolean) {
        val normalizedPostalCode = postalCode.filter(Char::isDigit).take(SWISS_POSTAL_CODE_LENGTH)
        if (isAdditionalDriver && uiState.value.isMainDriverSelf != false) return

        if (isAdditionalDriver) {
            additionalPostalCodeLookupJob?.cancel()
            additionalPostalCodeLookupJob = null
            _uiState.update {
                it.copy(
                    additionalDriverPostalCode = normalizedPostalCode,
                    additionalDriverCity = null,
                    additionalPostalCodeCityError = null,
                    isAdditionalPostalCodeCityLoading = false,
                )
            }
        } else {
            postalCodeLookupJob?.cancel()
            postalCodeLookupJob = null
            _uiState.update {
                it.copy(
                    postalCode = normalizedPostalCode,
                    postalCodeCity = null,
                    postalCodeCityError = null,
                    isPostalCodeCityLoading = false,
                )
            }
        }

        if (normalizedPostalCode.length != SWISS_POSTAL_CODE_LENGTH) return

        val lookupJob =
            viewModelScope.launch {
                delay(POSTAL_CODE_LOOKUP_DEBOUNCE_MS)
                val latestState = uiState.value
                val currentPostalCode =
                    if (isAdditionalDriver) {
                        if (latestState.isMainDriverSelf != false) return@launch
                        latestState.additionalDriverPostalCode
                    } else {
                        latestState.postalCode
                    }
                if (currentPostalCode != normalizedPostalCode) return@launch

                _uiState.update {
                    if (isAdditionalDriver) {
                        it.copy(isAdditionalPostalCodeCityLoading = true)
                    } else {
                        it.copy(isPostalCodeCityLoading = true)
                    }
                }

                val result = getCityByPostalCodeUseCase(normalizedPostalCode)
                _uiState.update {
                    val latestPostalCode =
                        if (isAdditionalDriver) {
                            it.additionalDriverPostalCode
                        } else {
                            it.postalCode
                        }
                    if (latestPostalCode != normalizedPostalCode ||
                        (isAdditionalDriver && it.isMainDriverSelf != false)
                    ) {
                        it
                    } else if (isAdditionalDriver) {
                        when (result) {
                            is CityLookupResult.Success ->
                                it.copy(
                                    additionalDriverCity = result.city,
                                    additionalPostalCodeCityError = null,
                                    isAdditionalPostalCodeCityLoading = false,
                                )
                            is CityLookupResult.Failure ->
                                it.copy(
                                    additionalDriverCity = null,
                                    additionalPostalCodeCityError = result.error,
                                    isAdditionalPostalCodeCityLoading = false,
                                )
                        }
                    } else {
                        when (result) {
                            is CityLookupResult.Success ->
                                it.copy(
                                    postalCodeCity = result.city,
                                    postalCodeCityError = null,
                                    isPostalCodeCityLoading = false,
                                )
                            is CityLookupResult.Failure ->
                                it.copy(
                                    postalCodeCity = null,
                                    postalCodeCityError = result.error,
                                    isPostalCodeCityLoading = false,
                                )
                        }
                    }
                }
            }
        if (isAdditionalDriver) {
            additionalPostalCodeLookupJob = lookupJob
        } else {
            postalCodeLookupJob = lookupJob
        }
    }

    private companion object {
        const val MIN_NEW_VALUE_ACCESSORIES = 3_800
        const val SWISS_POSTAL_CODE_LENGTH = 4
        const val POSTAL_CODE_LOOKUP_DEBOUNCE_MS = 400L
        const val SECONDS_PER_DAY = 86_400L
        const val DAYS_TO_UNIX_EPOCH = 719_468L
        const val DAYS_PER_400_YEARS = 146_097L
    }
}

data class DriverInformationUiState(
    val car: Car? = null,
    val vehicleModelError: NetworkError? = null,
    val offerConfiguration: OfferConfiguration? = null,
    val offerConfigurationError: NetworkError? = null,
    val isLeased: Boolean? = null,
    val isCompany: Boolean? = null,
    val firstName: String = "",
    val postalCode: String = "",
    val newValueAccessories: String = "",
    val newValueAccessoriesMinimum: Int = 3_800,
    val postalCodeCity: RecommendationCity? = null,
    val isPostalCodeCityLoading: Boolean = false,
    val postalCodeCityError: NetworkError? = null,
    val vehicleUsage: String? = null,
    val isMainDriverSelf: Boolean? = null,
    val gender: String? = null,
    val dateOfBirth: String = "",
    val nationality: String = "Schweiz",
    val additionalDriverPostalCode: String = "",
    val additionalDriverCity: RecommendationCity? = null,
    val isAdditionalPostalCodeCityLoading: Boolean = false,
    val additionalPostalCodeCityError: NetworkError? = null,
    val drivingTestYear: String = "",
    val hadDamageInLastThreeYears: Boolean? = null,
    val damageType: String? = null,
    val damageYear: Int? = null,
    val damageFault: String? = null,
    val damageSum: String = "",
    val insuranceStartDate: String = "",
    val paymentInterval: PaymentInterval? = null,
    val isRecommendationLoading: Boolean = false,
    val recommendationError: NetworkError? = null,
    val hasRecommendationInputError: Boolean = false,
)

enum class PaymentInterval(val apiCode: String) {
    ANNUAL("JAEHRLICH"),
    SEMIANNUAL("HALBJAEHRLICH"),
    MONTHLY("MONATLICH"),
}