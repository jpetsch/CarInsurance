package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult
import com.jpapplications.carinsurance.domain.repository.OfferConfigurationRepository
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

internal class OfferConfigurationRepositoryImpl(
    private val service: CarInsuranceService,
) : OfferConfigurationRepository {
    override suspend fun getOfferConfiguration(): OfferConfigurationResult {
        return try {
            OfferConfigurationResult.Success(service.offerConfiguration().toDomain())
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error =
                if (exception.response.status.value >= 500) {
                    NetworkError.SERVER
                } else {
                    NetworkError.REQUEST_REJECTED
                }
            OfferConfigurationResult.Failure(error)
        } catch (_: SerializationException) {
            OfferConfigurationResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: UnsupportedOfferConfigurationValueException) {
            OfferConfigurationResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: NoTransformationFoundException) {
            OfferConfigurationResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: IOException) {
            OfferConfigurationResult.Failure(NetworkError.NETWORK)
        }

    }
}

internal class UnsupportedOfferConfigurationValueException(
    valueType: String,
    apiCode: String,
) : Exception("Unsupported $valueType API code: $apiCode")
