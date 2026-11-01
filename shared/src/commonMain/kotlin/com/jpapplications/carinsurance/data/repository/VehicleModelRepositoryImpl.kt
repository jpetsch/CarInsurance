package com.jpapplications.carinsurance.data.repository

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.repository.VehicleModelRepository
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

class VehicleModelRepositoryImpl(
    private val service: CarInsuranceService,
) : VehicleModelRepository {
    override suspend fun getVehicleModel(etxCode: Int): VehicleModelResult {
        return try {
            VehicleModelResult.Success(service.vehicleModel(etxCode).toDomain())
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error =
                if (exception.response.status.value >= 500) {
                    NetworkError.SERVER
                } else {
                    NetworkError.REQUEST_REJECTED
                }
            VehicleModelResult.Failure(error)
        } catch (_: SerializationException) {
            VehicleModelResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: NoTransformationFoundException) {
            VehicleModelResult.Failure(NetworkError.INVALID_RESPONSE)
        } catch (_: IOException) {
            VehicleModelResult.Failure(NetworkError.NETWORK)
        }
    }
}
