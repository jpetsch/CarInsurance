package com.jpapplications.carinsurance.data.datasource

import com.jpapplications.carinsurance.data.model.CarSearchDto
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class CarInsuranceService(
    private val httpClient: HttpClient,
) {
    private val baseUrl = "https://app.int.smile-insurances.ch"

    suspend fun vehicleSearch(request: CarSearchRequestDto): Map<String, List<CarSearchDto>> =
        httpClient.get {
            url("$baseUrl/api/v1/car/vehicle/search/fulltext")
            parameter("vehicle-type", request.vehicleType)
            parameter("search-term", request.searchTerm)
            request.year?.let { parameter("year", it) }
        }.body()
}