package com.jpapplications.carinsurance.presentation.screens.driverInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfiguration
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.usecase.GetOfferConfigurationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DriverInformationViewModel(
    private val etxCode: Int,
    private val getVehicleModelUseCase: GetVehicleModelUseCase,
    private val getOfferConfigurationUseCase: GetOfferConfigurationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DriverInformationUiState())
    val uiState: StateFlow<DriverInformationUiState> = _uiState.asStateFlow()

    init {
        loadCarDetails()
        loadOfferConfiguration()
    }

    sealed interface DriverInformationEvent {
        data class LeasingChanged(val isLeased: Boolean) : DriverInformationEvent
        data class RegistrationOwnerChanged(val isCompany: Boolean) : DriverInformationEvent
        data class FirstNameChanged(val firstName: String) : DriverInformationEvent
        data class PostalCodeChanged(val postalCode: String) : DriverInformationEvent
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
        data class PaymentIntervalChanged(val interval: String) : DriverInformationEvent
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
                _uiState.update {
                    it.copy(postalCode = event.postalCode.filter(Char::isDigit).take(5))
                }
            is DriverInformationEvent.VehicleUsageChanged ->
                _uiState.update { it.copy(vehicleUsage = event.vehicleUsage) }
            is DriverInformationEvent.MainDriverChanged ->
                _uiState.update {
                    it.copy(
                        isMainDriverSelf = event.isSelf,
                        additionalDriverPostalCode =
                            if (event.isSelf) "" else it.additionalDriverPostalCode,
                    )
                }
            is DriverInformationEvent.GenderChanged ->
                _uiState.update { it.copy(gender = event.gender) }
            is DriverInformationEvent.DateOfBirthChanged ->
                _uiState.update { it.copy(dateOfBirth = formatDateOfBirth(event.dateOfBirth)) }
            is DriverInformationEvent.NationalityChanged ->
                _uiState.update { it.copy(nationality = event.nationality) }
            is DriverInformationEvent.AdditionalDriverPostalCodeChanged ->
                _uiState.update {
                    if (it.isMainDriverSelf == false) {
                        it.copy(
                            additionalDriverPostalCode =
                                event.postalCode.filter(Char::isDigit).take(5),
                        )
                    } else {
                        it
                    }
                }
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
            DriverInformationEvent.RecommendationRequested -> Unit
        }
    }

    private fun loadCarDetails() {
        viewModelScope.launch {
            when(val result = getVehicleModelUseCase(etxCode)) {
                is VehicleModelResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            car = result.car,
                        )
                    }
                }
                is VehicleModelResult.Failure -> TODO("Not yet implemented")
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

    private fun formatDateOfBirth(value: String): String {
        val digits = value.filter(Char::isDigit).take(8)
        return buildString {
            digits.forEachIndexed { index, digit ->
                if (index == 2 || index == 4) append('.')
                append(digit)
            }
        }
    }

}

data class DriverInformationUiState(
    val car: Car? = null,
    val offerConfiguration: OfferConfiguration? = null,
    val offerConfigurationError: NetworkError? = null,
    val isLeased: Boolean? = null,
    val isCompany: Boolean? = null,
    val firstName: String = "",
    val postalCode: String = "",
    val vehicleUsage: String? = null,
    val isMainDriverSelf: Boolean? = null,
    val gender: String? = null,
    val dateOfBirth: String = "",
    val nationality: String = "Schweiz",
    val additionalDriverPostalCode: String = "",
    val drivingTestYear: String = "",
    val hadDamageInLastThreeYears: Boolean? = null,
    val damageType: String? = null,
    val damageYear: Int? = null,
    val damageFault: String? = null,
    val damageSum: String = "",
    val insuranceStartDate: String = "",
    val paymentInterval: String? = null,
)