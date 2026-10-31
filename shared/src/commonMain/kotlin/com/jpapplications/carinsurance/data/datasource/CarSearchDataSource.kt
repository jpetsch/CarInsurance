package com.jpapplications.carinsurance.data.datasource

import com.jpapplications.carinsurance.data.model.CarSearchDto
import com.jpapplications.carinsurance.data.model.CarSearchRequestDto

interface CarSearchDataSource {
    suspend fun searchCars(request: CarSearchRequestDto): List<CarSearchDto>
}
