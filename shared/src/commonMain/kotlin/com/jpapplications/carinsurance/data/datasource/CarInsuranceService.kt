package com.jpapplications.carinsurance.data.datasource

import com.jpapplications.carinsurance.data.model.CarDto
import com.jpapplications.carinsurance.data.model.CarRecommendationDto
import com.jpapplications.carinsurance.data.model.CarRecommendationRequestDto
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto
import com.jpapplications.carinsurance.data.model.OfferConfigurationDto
import com.jpapplications.carinsurance.data.model.RecommendationCityDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class CarInsuranceService(
    private val httpClient: HttpClient,
) {
    private val baseUrl = "https://app.int.smile-insurances.ch"

    suspend fun vehicleSearch(request: CarSearchRequestDto): Map<String, List<CarDto>> =
        httpClient.get {
            url("$baseUrl/api/v1/car/vehicle/search/fulltext")
            parameter("vehicle-type", request.vehicleType)
            parameter("search-term", request.searchTerm)
            request.year?.let { parameter("year", it) }
        }.body()

    suspend fun vehicleModel(etxCode: Int): CarDto =
        httpClient.get {
            url("$baseUrl/api/v1/car/vehicle/model")
            parameter("etxCode", etxCode)
        }.body()

    suspend fun offerConfiguration(): OfferConfigurationDto =
        httpClient.get {
            url("$baseUrl/api/v1/car/offer/config")
        }.body()

    suspend fun cities(postalCode: String): List<RecommendationCityDto> =
        httpClient.get {
            url("$baseUrl/api/v1/cities")
            parameter("zipcode", postalCode)
        }.body()

    suspend fun recommendation(
        request: CarRecommendationRequestDto,
        language: String,
    ): CarRecommendationDto =
        httpClient.post {
            url("$baseUrl/api/v1/car/offer/recommendation")
            parameter("language", language)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}