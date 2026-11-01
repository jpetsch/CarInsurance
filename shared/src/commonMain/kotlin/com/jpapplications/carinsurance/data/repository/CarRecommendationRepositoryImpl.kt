package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.domain.model.CarRecommendationRequest
import com.jpapplications.carinsurance.domain.model.CarRecommendationResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.repository.CarRecommendationRepository
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

internal class CarRecommendationRepositoryImpl(
    private val service: CarInsuranceService,
) : CarRecommendationRepository {
    override suspend fun getRecommendation(
        request: CarRecommendationRequest,
        language: String,
    ): CarRecommendationResult {
        return try {
            CarRecommendationResult.Success(
                service.recommendation(request.toDto(), language).toDomain(),
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error =
                if (exception.response.status.value >= 500) {
                    NetworkError.SERVER
                } else {
                    NetworkError.REQUEST_REJECTED
                }
            CarRecommendationResult.Failure(error)
        } catch (_: SerializationException) {
            CarRecommendationResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: NoTransformationFoundException) {
            CarRecommendationResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: IOException) {
            CarRecommendationResult.Failure(NetworkError.NETWORK)
        }
    }
}
