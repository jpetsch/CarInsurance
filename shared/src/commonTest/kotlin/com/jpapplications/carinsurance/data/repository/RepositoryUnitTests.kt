package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.domain.carFixture
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.recommendationRequestFixture
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RepositoryUnitTests {
    @Test
    fun carSearchRepositoryPreservesGroupsMapsCarsAndSendsQuery() = runTest {
        var requestUrl: Url? = null
        val service =
            service { request ->
                requestUrl = request.url
                respond(carGroupsJson)
            }

        val result =
            CarSearchRepositoryImpl(service).searchCars(
                CarSearchCriteria(searchTerm = "BMW 3", year = 2024),
            )

        val success = assertIs<CarSearchResult.Success>(result)
        assertEquals(listOf("BMW 3 Series"), success.groups.keys.toList())
        assertEquals(carFixture(), success.groups.getValue("BMW 3 Series").single())
        assertEquals("/api/v1/car/vehicle/search/fulltext", requestUrl?.encodedPath)
        assertEquals("CAR", requestUrl?.parameters?.get("vehicle-type"))
        assertEquals("BMW 3", requestUrl?.parameters?.get("search-term"))
        assertEquals("2024", requestUrl?.parameters?.get("year"))
    }

    @Test
    fun vehicleModelRepositoryMapsCarAndSendsEtxCode() = runTest {
        var requestUrl: Url? = null
        val result =
            VehicleModelRepositoryImpl(
                service { request ->
                    requestUrl = request.url
                    respond(carJson)
                },
            ).getVehicleModel(12345)

        assertEquals(VehicleModelResult.Success(carFixture()), result)
        assertEquals("/api/v1/car/vehicle/model", requestUrl?.encodedPath)
        assertEquals("12345", requestUrl?.parameters?.get("etxCode"))
    }

    @Test
    fun offerConfigurationRepositoryMapsSupportedApiValues() = runTest {
        val result =
            OfferConfigurationRepositoryImpl(
                service { respond(offerConfigurationJson) },
            ).getOfferConfiguration()

        val configuration = assertIs<OfferConfigurationResult.Success>(result).configuration
        assertEquals(listOf(com.jpapplications.carinsurance.domain.model.Gender.Known.MALE), configuration.gender)
        assertEquals(
            listOf(com.jpapplications.carinsurance.domain.model.VehicleUsage.Known.PRIVATE),
            configuration.fahrzeugNutzung,
        )
        assertEquals(100, configuration.maxAge)
    }

    @Test
    fun cityRepositoryReturnsFirstCity() = runTest {
        var requestUrl: Url? = null
        val result =
            CityRepositoryImpl(
                service { request ->
                    requestUrl = request.url
                    respond("""[{"plz":8000,"name":"Zürich","kanton":"ZH"},{"plz":8001,"name":"Zürich","kanton":"ZH"}]""")
                },
            ).getCity("8000")

        assertEquals(
            CityLookupResult.Success(
                com.jpapplications.carinsurance.domain.model.RecommendationCity(8000, "Zürich", "ZH"),
            ),
            result,
        )
        assertEquals("/api/v1/cities", requestUrl?.encodedPath)
        assertEquals("8000", requestUrl?.parameters?.get("zipcode"))
    }

    @Test
    fun cityRepositoryRejectsAnEmptyResponse() = runTest {
        val result =
            CityRepositoryImpl(
                service { respond("[]") },
            ).getCity("8000")

        assertEquals(CityLookupResult.Failure(NetworkError.INVALID_RESPONSE), result)
    }

    @Test
    fun carRecommendationRepositoryPostsRequestAndMapsResponse() = runTest {
        var requestUrl: Url? = null
        var requestMethod: HttpMethod? = null
        val result =
            CarRecommendationRepositoryImpl(
                service { request ->
                    requestUrl = request.url
                    requestMethod = request.method
                    respond(recommendationJson)
                },
            ).getRecommendation(recommendationRequestFixture(), language = "fr")

        val recommendation =
            assertIs<com.jpapplications.carinsurance.domain.model.CarRecommendationResult.Success>(result)
                .recommendation
        assertEquals(1293.9, recommendation.gesamtBruttoPraemie)
        assertEquals("recommendation-token", recommendation.krf)
        assertEquals("/api/v1/car/offer/recommendation", requestUrl?.encodedPath)
        assertEquals("fr", requestUrl?.parameters?.get("language"))
        assertEquals(HttpMethod.Post, requestMethod)
    }

    @Test
    fun repositoriesTranslateHttpServerAndClientErrors() = runTest {
        val serverError =
            CarSearchRepositoryImpl(
                service {
                    respond("{}", status = HttpStatusCode.InternalServerError)
                },
            ).searchCars(CarSearchCriteria("BMW", null))
        val rejectedRequest =
            VehicleModelRepositoryImpl(
                service {
                    respond("{}", status = HttpStatusCode.BadRequest)
                },
            ).getVehicleModel(12345)

        assertEquals(CarSearchResult.Failure(NetworkError.SERVER), serverError)
        assertEquals(VehicleModelResult.Failure(NetworkError.REQUEST_REJECTED), rejectedRequest)
    }

    @Test
    fun repositoryTranslatesIoFailuresToNetworkErrors() = runTest {
        val result =
            CarSearchRepositoryImpl(
                service { throw IOException("offline") },
            ).searchCars(CarSearchCriteria("BMW", null))

        assertEquals(CarSearchResult.Failure(NetworkError.NETWORK), result)
    }

    @Test
    fun offerConfigurationRepositoryRejectsUnsupportedDamageCodes() = runTest {
        val unsupportedConfiguration =
            OfferConfigurationRepositoryImpl(
                service {
                    respond(offerConfigurationJson.replace("LIABILITY_MATERIAL", "UNKNOWN_DAMAGE"))
                },
            ).getOfferConfiguration()

        assertEquals(
            OfferConfigurationResult.Failure(NetworkError.INVALID_RESPONSE),
            unsupportedConfiguration,
        )
    }

    @Test
    fun searchWithoutYearOmitsYearParameter() = runTest {
        var requestUrl: Url? = null
        val result =
            CarSearchRepositoryImpl(
                service { request ->
                    requestUrl = request.url
                    respond(carGroupsJson)
                },
            ).searchCars(CarSearchCriteria("BMW", null))

        assertIs<CarSearchResult.Success>(result)
        assertNull(requestUrl?.parameters?.get("year"))
        assertTrue(requestUrl?.parameters?.contains("search-term") == true)
    }

    private fun service(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): CarInsuranceService {
        val client =
            HttpClient(MockEngine(handler)) {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
        return CarInsuranceService(client)
    }

    private suspend fun MockRequestHandleScope.respond(content: String) =
        respond(
            content = content,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        )

    private companion object {
        val carJson =
            """
            {
              "matriculationDate": 2024,
              "constructionStartYear": 2023,
              "catalogPrice": 50000,
              "etxCode": 12345,
              "description": "Test car",
              "capacity": 2000,
              "doors": 5,
              "seats": 5,
              "emptyLoad": 1500,
              "horsePower": 150,
              "enginePowerInKw": 110,
              "vehicleType": "CAR",
              "vehicleClass": 1,
              "constructionType": "SUV",
              "fuel": "PETROL",
              "brand": "Example",
              "model": "Model",
              "eMobility": false
            }
            """.trimIndent()

        val carGroupsJson = """{"BMW 3 Series":[$carJson]}"""

        val offerConfigurationJson =
            """
            {
              "gender": ["MALE"],
              "schadensfallTyp": ["LIABILITY_MATERIAL"],
              "schadensfallSchuld": ["YES_SELF_PAYMENT"],
              "finanzierung": ["SELF"],
              "fahrzeugNutzung": ["PRIVAT"],
              "fahrzeugNutzungAllowedForBusiness": ["BUSINESS"],
              "vnTyp": [],
              "halterTyp": ["HAEUFIGSTERLENKER"],
              "schadenfallYear": [2025],
              "maxAge": 100
            }
            """.trimIndent()

        val recommendationJson =
            """
            {
              "gesamtBruttoPraemie": 1293.9,
              "metaData": {
                "uwfLiability": "uwf-liability",
                "cffLiability": "cff-liability",
                "uwfAssistance": "uwf-assistance",
                "cffAssistance": "cff-assistance",
                "uwfHull": "uwf-hull",
                "cffHullTk": "cff-hull-tk",
                "cffHullVk": "cff-hull-vk",
                "uwfPassenger": "uwf-passenger",
                "cffPassenger": "cff-passenger",
                "keepOnChanges": false
              },
              "krf": "recommendation-token"
            }
            """.trimIndent()
    }
}
