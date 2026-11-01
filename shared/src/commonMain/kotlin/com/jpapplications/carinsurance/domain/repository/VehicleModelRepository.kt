package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.VehicleModelResult

interface VehicleModelRepository {
    suspend fun getVehicleModel(etxCode: Int): VehicleModelResult
}
