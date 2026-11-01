package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.domain.model.CityLookupResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.repository.CityRepository
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

internal class CityRepositoryImpl(
    private val service: CarInsuranceService,
) : CityRepository {
    override suspend fun getCity(postalCode: String): CityLookupResult {
        return try {
            val city =
                service.cities(postalCode).firstOrNull()?.toDomain()
                    ?: return CityLookupResult.Failure(NetworkError.INVALID_RESPONSE)
            CityLookupResult.Success(city)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error =
                if (exception.response.status.value >= 500) {
                    NetworkError.SERVER
                } else {
                    NetworkError.REQUEST_REJECTED
                }
            CityLookupResult.Failure(error)
        } catch (_: SerializationException) {
            CityLookupResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: NoTransformationFoundException) {
            CityLookupResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: IOException) {
            CityLookupResult.Failure(NetworkError.NETWORK)
        }
    }
}
