package com.jpapplications.carinsurance.domain

import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.repository.CarRecommendationRepository
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import com.jpapplications.carinsurance.domain.repository.CityRepository
import com.jpapplications.carinsurance.domain.repository.OfferConfigurationRepository
import com.jpapplications.carinsurance.domain.repository.VehicleModelRepository
import com.jpapplications.carinsurance.domain.usecase.GetCarRecommendationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetCityByPostalCodeUseCase
import com.jpapplications.carinsurance.domain.usecase.GetOfferConfigurationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

class DomainUnitTests {
    @Test
    fun carSearchCriteriaAcceptsBoundaryYearsAndMissingYear() {
        assertEquals(
            CarSearchCriteria.MIN_YEAR,
            CarSearchCriteria("BMW", CarSearchCriteria.MIN_YEAR).year,
        )
        assertEquals(
            CarSearchCriteria.MAX_YEAR,
            CarSearchCriteria("BMW", CarSearchCriteria.MAX_YEAR).year,
        )
        assertEquals(null, CarSearchCriteria("BMW", null).year)
    }

    @Test
    fun carSearchCriteriaRejectsOutOfRangeYears() {
        assertFailsWith<IllegalArgumentException> {
            CarSearchCriteria("BMW", CarSearchCriteria.MIN_YEAR - 1)
        }
        assertFailsWith<IllegalArgumentException> {
            CarSearchCriteria("BMW", CarSearchCriteria.MAX_YEAR + 1)
        }
    }

    @Test
    fun searchCarsUseCaseForwardsCriteriaAndReturnsRepositoryResult() = runTest {
        val expected = CarSearchResult.Failure(NetworkError.NETWORK)
        var received: CarSearchCriteria? = null
        val useCase =
            SearchCarsUseCase(
                object : CarSearchRepository {
                    override suspend fun searchCars(criteria: CarSearchCriteria): CarSearchResult {
                        received = criteria
                        return expected
                    }
                },
            )
        val criteria = CarSearchCriteria("BMW", 2024)

        assertEquals(expected, useCase(criteria))
        assertEquals(criteria, received)
    }

    @Test
    fun getVehicleModelUseCaseForwardsCodeAndResult() = runTest {
        val expected = VehicleModelResult.Failure(NetworkError.SERVER)
        var received: Int? = null
        val useCase =
            GetVehicleModelUseCase(
                object : VehicleModelRepository {
                    override suspend fun getVehicleModel(etxCode: Int): VehicleModelResult {
                        received = etxCode
                        return expected
                    }
                },
            )

        assertEquals(expected, useCase(12345))
        assertEquals(12345, received)
    }

    @Test
    fun getOfferConfigurationUseCaseReturnsRepositoryResult() = runTest {
        val expected = OfferConfigurationResult.Failure(NetworkError.INVALID_RESPONSE)
        val useCase =
            GetOfferConfigurationUseCase(
                object : OfferConfigurationRepository {
                    override suspend fun getOfferConfiguration() = expected
                },
            )

        assertEquals(expected, useCase())
    }

    @Test
    fun getCityByPostalCodeUseCaseForwardsPostalCodeAndResult() = runTest {
        val expected = CityLookupResult.Failure(NetworkError.REQUEST_REJECTED)
        var received: String? = null
        val useCase =
            GetCityByPostalCodeUseCase(
                object : CityRepository {
                    override suspend fun getCity(postalCode: String): CityLookupResult {
                        received = postalCode
                        return expected
                    }
                },
            )

        assertEquals(expected, useCase("8000"))
        assertEquals("8000", received)
    }

    @Test
    fun getCarRecommendationUseCaseForwardsRequestLanguageAndResult() = runTest {
        val expected = CarRecommendationResult.Success(recommendationFixture())
        val request = recommendationRequestFixture()
        var receivedRequest: CarRecommendationRequest? = null
        var receivedLanguage: String? = null
        val useCase =
            GetCarRecommendationUseCase(
                object : CarRecommendationRepository {
                    override suspend fun getRecommendation(
                        request: CarRecommendationRequest,
                        language: String,
                    ): CarRecommendationResult {
                        receivedRequest = request
                        receivedLanguage = language
                        return expected
                    }
                },
            )

        assertEquals(expected, useCase(request, "fr"))
        assertEquals(request, receivedRequest)
        assertEquals("fr", receivedLanguage)

        assertEquals(expected, useCase(request))
        assertEquals("de", receivedLanguage)
    }
}

internal fun recommendationRequestFixture(): CarRecommendationRequest =
    CarRecommendationRequest(
        versicherungsBeginn = "2027-01-01",
        zahlungsintervall = "JAEHRLICH",
        halterTyp = "HAEUFIGSTERLENKER",
        fahrzeug =
            com.jpapplications.carinsurance.domain.model.RecommendationVehicle(
                car = carFixture(),
                matriculationDate = 2024,
                newValueAccessories = 5_000,
                financing = "SELF",
                vehicleUsage = com.jpapplications.carinsurance.domain.model.VehicleUsage.Known.PRIVATE,
            ),
        fahrzeugLenker =
            com.jpapplications.carinsurance.domain.model.RecommendationDriver(
                geschlecht = com.jpapplications.carinsurance.domain.model.Gender.Known.MALE,
                geburtsdatum = "1990-06-15",
                nationalitaet = "CH",
                stadt =
                    com.jpapplications.carinsurance.domain.model.RecommendationCity(
                        plz = 8000,
                        name = "Zürich",
                        kanton = "ZH",
                    ),
                jahrFahrpruefungPkw = 2008,
            ),
    )

internal fun recommendationFixture() =
    CarRecommendation(
        gesamtBruttoPraemie = 1200.0,
        metaData =
            com.jpapplications.carinsurance.domain.model.RecommendationMetaData(
                uwfLiability = "",
                cffLiability = "",
                uwfAssistance = "",
                cffAssistance = "",
                uwfHull = "",
                cffHullTk = "",
                cffHullVk = "",
                uwfPassenger = "",
                cffPassenger = "",
                keepOnChanges = false,
            ),
        krf = "test-token",
    )

internal fun carFixture(
    catalogPrice: Int = 50_000,
    etxCode: Int = 12345,
) = com.jpapplications.carinsurance.domain.model.Car(
    matriculationDate = 2024,
    constructionStartYear = 2023,
    catalogPrice = catalogPrice,
    etxCode = etxCode,
    description = "Test car",
    capacity = 2000,
    model = "Model",
    doors = 5,
    seats = 5,
    emptyLoad = 1500,
    horsePower = 150,
    enginePowerInKw = 110,
    vehicleType = "CAR",
    vehicleClass = 1,
    constructionType = "SUV",
    fuel = "PETROL",
    brand = "Example",
    eMobility = false,
)
