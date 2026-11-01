package com.jpapplications.carinsurance.domain.usecase

import com.jpapplications.carinsurance.domain.model.VehicleModelResult
import com.jpapplications.carinsurance.domain.repository.VehicleModelRepository

class GetVehicleModelUseCase(
    private val repository: VehicleModelRepository,
) {
    suspend operator fun invoke(etxCode: Int): VehicleModelResult =
        repository.getVehicleModel(etxCode)
}
