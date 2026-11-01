package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.data.model.CarDto
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto
import com.jpapplications.carinsurance.domain.model.Car
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchError
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

class CarSearchRepositoryImpl(
    private val service: CarInsuranceService,
) : CarSearchRepository {
    override suspend fun searchCars(criteria: CarSearchCriteria): CarSearchResult {
        return try {
            val request =
                CarSearchRequestDto(
                    vehicleType = criteria.vehicleType.name,
                    searchTerm = criteria.searchTerm,
                    year = criteria.year,
                )

            CarSearchResult.Success(
                service.vehicleSearch(request).mapValues { (_, cars) ->
                    cars.map(CarDto::toDomain)
                },
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error =
                if (exception.response.status.value >= 500) {
                    CarSearchError.SERVER
                } else {
                    CarSearchError.REQUEST_REJECTED
                }
            CarSearchResult.Failure(error)
        } catch (_: SerializationException) {
            CarSearchResult.Failure(CarSearchError.INVALID_RESPONSE)
        } catch (_: NoTransformationFoundException) {
            CarSearchResult.Failure(CarSearchError.INVALID_RESPONSE)
        } catch (_: IOException) {
            CarSearchResult.Failure(CarSearchError.NETWORK)
        }
    }
}

private fun CarDto.toDomain() =
    Car(
        matriculationDate = matriculationDate,
        constructionStartYear = constructionStartYear,
        catalogPrice = catalogPrice,
        etxCode = etxCode,
        description = description,
        capacity = capacity,
        model = model,
        doors = doors,
        seats = seats,
        emptyLoad = emptyLoad,
        horsePower = horsePower,
        enginePowerInKw = enginePowerInKw,
        vehicleType = vehicleType,
        vehicleClass = vehicleClass,
        constructionType = constructionType,
        fuel = fuel,
        brand = brand,
        eMobility = eMobility,
    )
