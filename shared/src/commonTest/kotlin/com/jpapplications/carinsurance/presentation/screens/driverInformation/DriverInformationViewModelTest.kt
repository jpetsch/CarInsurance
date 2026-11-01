package com.jpapplications.carinsurance.presentation.screens.driverInformation

import com.jpapplications.carinsurance.domain.carFixture
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult
import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.model.DamageFault
import com.jpapplications.carinsurance.domain.model.DamageType
import com.jpapplications.carinsurance.domain.model.Gender
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfiguration
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.model.RecommendationCity
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.model.VehicleUsage
import com.jpapplications.carinsurance.domain.repository.CarRecommendationRepository
import com.jpapplications.carinsurance.domain.repository.CityRepository
import com.jpapplications.carinsurance.domain.repository.OfferConfigurationRepository
import com.jpapplications.carinsurance.domain.repository.VehicleModelRepository
import com.jpapplications.carinsurance.domain.recommendationFixture
import com.jpapplications.carinsurance.domain.usecase.GetCarRecommendationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetCityByPostalCodeUseCase
import com.jpapplications.carinsurance.domain.usecase.GetOfferConfigurationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import com.jpapplications.carinsurance.presentation.runViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DriverInformationViewModelTest {
    @Test
    fun initializationLoadsVehicleAndOfferConfiguration() = runViewModelTest {
        val viewModel = createViewModel()
        runCurrent()

        assertEquals(carFixture(), viewModel.uiState.value.car)
        assertEquals(5_000, viewModel.uiState.value.newValueAccessoriesMinimum)
        assertEquals("5000", viewModel.uiState.value.newValueAccessories)
        assertEquals(configurationFixture(), viewModel.uiState.value.offerConfiguration)
        assertNull(viewModel.uiState.value.vehicleModelError)
        assertNull(viewModel.uiState.value.offerConfigurationError)
    }

    @Test
    fun inputEventsNormalizeAccessoriesDatesAndClearDamageDetails() = runViewModelTest {
        val viewModel = createViewModel()
        runCurrent()

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.NewValueAccessoriesChanged("12a"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.NewValueAccessoriesFocusChanged(false))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DateOfBirthChanged("15061990"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DrivingTestYearChanged("20a08"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(true))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageTypeChanged(DamageType.HAIL))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageYearChanged(2025))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageFaultChanged(DamageFault.YES_SELF_PAYMENT))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageSumChanged("1200"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(false))

        val state = viewModel.uiState.value
        assertEquals("5000", state.newValueAccessories)
        assertEquals("15.06.1990", state.dateOfBirth)
        assertEquals("2008", state.drivingTestYear)
        assertFalse(state.hadDamageInLastThreeYears!!)
        assertNull(state.damageType)
        assertNull(state.damageYear)
        assertNull(state.damageFault)
        assertEquals("", state.damageSum)
    }

    @Test
    fun postalCodeLookupIsDebouncedAndUsesNormalizedValue() = runViewModelTest {
        val cityRepository = RecordingCityRepository()
        val viewModel = createViewModel(cityRepository = cityRepository)
        runCurrent()

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.PostalCodeChanged("80-00"))
        runCurrent()
        advanceTimeBy(399)
        runCurrent()
        assertTrue(cityRepository.postalCodes.isEmpty())

        advanceTimeBy(1)
        runCurrent()
        assertEquals(listOf("8000"), cityRepository.postalCodes)
        assertEquals(RecommendationCity(8000, "Zürich", "ZH"), viewModel.uiState.value.postalCodeCity)
        assertFalse(viewModel.uiState.value.isPostalCodeCityLoading)
    }

    @Test
    fun additionalDriverPostalCodeOnlyResolvesWhenAnotherDriverIsSelected() = runViewModelTest {
        val cityRepository = RecordingCityRepository()
        val viewModel = createViewModel(cityRepository = cityRepository)
        runCurrent()

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.AdditionalDriverPostalCodeChanged("8000"))
        assertEquals("", viewModel.uiState.value.additionalDriverPostalCode)
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.MainDriverChanged(false))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.AdditionalDriverPostalCodeChanged("8000"))
        runCurrent()
        advanceTimeBy(400)
        runCurrent()

        assertEquals("8000", viewModel.uiState.value.additionalDriverPostalCode)
        assertEquals(RecommendationCity(8000, "Zürich", "ZH"), viewModel.uiState.value.additionalDriverCity)
        assertEquals(listOf("8000"), cityRepository.postalCodes)
    }

    @Test
    fun recommendationRequestUsesValidatedFormAndEmitsResult() = runViewModelTest {
        val recommendationRepository =
            RecordingRecommendationRepository(CarRecommendationResult.Success(recommendationFixture()))
        val viewModel =
            createViewModel(
                cityRepository = RecordingCityRepository(),
                recommendationRepository = recommendationRepository,
            )
        val emitted = mutableListOf<com.jpapplications.carinsurance.domain.model.CarRecommendation>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.showRecommendationEvent.collect(emitted::add)
        }
        runCurrent()
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.MainDriverChanged(true))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.LeasingChanged(false))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.VehicleUsageChanged(VehicleUsage.Known.PRIVATE))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.GenderChanged(Gender.Known.MALE))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.PostalCodeChanged("8000"))
        runCurrent()
        advanceTimeBy(400)
        runCurrent()
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DateOfBirthChanged("15061990"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.NationalityChanged("us"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DrivingTestYearChanged("2008"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(true))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageTypeChanged(DamageType.HAIL))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageYearChanged(2025))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageFaultChanged(DamageFault.YES_INSURANCE_PAYMENT))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageSumChanged("1250,50"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.InsuranceStartDateChanged("01.01.2027"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.PaymentIntervalChanged(PaymentInterval.ANNUAL))

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.RecommendationRequested)
        runCurrent()

        val request = recommendationRepository.request
        assertEquals("2027-01-01", request?.versicherungsBeginn)
        assertEquals("JAEHRLICH", request?.zahlungsintervall)
        assertEquals("HAEUFIGSTERLENKER", request?.halterTyp)
        assertEquals("US", request?.fahrzeugLenker?.nationalitaet)
        assertEquals(RecommendationCity(8000, "Zürich", "ZH"), request?.fahrzeugLenker?.stadt)
        assertEquals(1250.5, request?.fahrzeugLenker?.schadensfaelle?.single()?.summe)
        assertEquals(5_000, request?.fahrzeug?.newValueAccessories)
        assertEquals(listOf(recommendationFixture()), emitted)
        assertFalse(viewModel.uiState.value.isRecommendationLoading)
        assertFalse(viewModel.uiState.value.hasRecommendationInputError)
    }

    @Test
    fun invalidRecommendationInputsSetInputErrorWithoutCallingRepository() = runViewModelTest {
        val recommendationRepository = RecordingRecommendationRepository()
        val viewModel = createViewModel(recommendationRepository = recommendationRepository)
        runCurrent()

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.RecommendationRequested)
        runCurrent()

        assertTrue(viewModel.uiState.value.hasRecommendationInputError)
        assertFalse(viewModel.uiState.value.isRecommendationLoading)
        assertNull(recommendationRepository.request)
    }

    @Test
    fun recommendationFailureIsStoredInUiState() = runViewModelTest {
        val recommendationRepository =
            RecordingRecommendationRepository(CarRecommendationResult.Failure(NetworkError.SERVER))
        val viewModel = createViewModel(recommendationRepository = recommendationRepository)
        runCurrent()
        completeRequiredForm(viewModel)
        advanceTimeBy(400)
        runCurrent()

        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.RecommendationRequested)
        runCurrent()

        assertEquals(NetworkError.SERVER, viewModel.uiState.value.recommendationError)
        assertFalse(viewModel.uiState.value.isRecommendationLoading)
    }

    private fun createViewModel(
        cityRepository: RecordingCityRepository = RecordingCityRepository(),
        recommendationRepository: RecordingRecommendationRepository = RecordingRecommendationRepository(),
    ) = DriverInformationViewModel(
        etxCode = 12345,
        getVehicleModelUseCase =
            GetVehicleModelUseCase(
                object : VehicleModelRepository {
                    override suspend fun getVehicleModel(etxCode: Int) =
                        VehicleModelResult.Success(carFixture(etxCode = etxCode))
                },
            ),
        getOfferConfigurationUseCase =
            GetOfferConfigurationUseCase(
                object : OfferConfigurationRepository {
                    override suspend fun getOfferConfiguration() =
                        OfferConfigurationResult.Success(configurationFixture())
                },
            ),
        getCarRecommendationUseCase = GetCarRecommendationUseCase(recommendationRepository),
        getCityByPostalCodeUseCase = GetCityByPostalCodeUseCase(cityRepository),
    )

    private fun completeRequiredForm(viewModel: DriverInformationViewModel) {
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.MainDriverChanged(true))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.LeasingChanged(false))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.VehicleUsageChanged(VehicleUsage.Known.PRIVATE))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.GenderChanged(Gender.Known.MALE))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.PostalCodeChanged("8000"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DateOfBirthChanged("15061990"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DrivingTestYearChanged("2008"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.DamageInLastThreeYearsChanged(false))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.InsuranceStartDateChanged("01.01.2027"))
        viewModel.onEvent(DriverInformationViewModel.DriverInformationEvent.PaymentIntervalChanged(PaymentInterval.ANNUAL))
    }

    private class RecordingCityRepository : CityRepository {
        val postalCodes = mutableListOf<String>()

        override suspend fun getCity(postalCode: String): CityLookupResult {
            postalCodes += postalCode
            return CityLookupResult.Success(RecommendationCity(8000, "Zürich", "ZH"))
        }
    }

    private class RecordingRecommendationRepository(
        private val result: CarRecommendationResult =
            CarRecommendationResult.Failure(NetworkError.REQUEST_REJECTED),
    ) : CarRecommendationRepository {
        var request: com.jpapplications.carinsurance.domain.model.CarRecommendationRequest? = null

        override suspend fun getRecommendation(
            request: com.jpapplications.carinsurance.domain.model.CarRecommendationRequest,
            language: String,
        ): CarRecommendationResult {
            this.request = request
            return result
        }
    }

    private companion object {
        fun configurationFixture() =
            OfferConfiguration(
                gender = Gender.Known.entries,
                schadensfallTyp = DamageType.entries,
                schadensfallSchuld = DamageFault.entries,
                finanzierung = listOf("SELF", "LEASING"),
                fahrzeugNutzung = VehicleUsage.Known.entries,
                fahrzeugNutzungAllowedForBusiness = listOf(VehicleUsage.Known.BUSINESS),
                vnTyp = emptyList(),
                halterTyp = listOf("HAEUFIGSTERLENKER", "HALTER"),
                schadenfallYear = listOf(2024, 2025),
                maxAge = 100,
            )
    }
}
