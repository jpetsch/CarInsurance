package com.jpapplications.carinsurance.domain.repository

import com.jpapplications.carinsurance.domain.model.OfferConfigurationResult

interface OfferConfigurationRepository {
    suspend fun getOfferConfiguration(): OfferConfigurationResult
}
