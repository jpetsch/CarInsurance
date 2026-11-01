package com.jpapplications.carinsurance.di

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.data.network.provideHttpClient
import com.jpapplications.carinsurance.data.repository.CarRecommendationRepositoryImpl
import com.jpapplications.carinsurance.data.repository.CityRepositoryImpl
import com.jpapplications.carinsurance.data.repository.CarSearchRepositoryImpl
import com.jpapplications.carinsurance.data.repository.OfferConfigurationRepositoryImpl
import com.jpapplications.carinsurance.data.repository.VehicleModelRepositoryImpl
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.repository.CarRecommendationRepository
import com.jpapplications.carinsurance.domain.repository.CityRepository
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import com.jpapplications.carinsurance.domain.repository.OfferConfigurationRepository
import com.jpapplications.carinsurance.domain.repository.VehicleModelRepository
import com.jpapplications.carinsurance.domain.usecase.GetCarRecommendationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetCityByPostalCodeUseCase
import com.jpapplications.carinsurance.domain.usecase.GetOfferConfigurationUseCase
import com.jpapplications.carinsurance.domain.usecase.GetVehicleModelUseCase
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import com.jpapplications.carinsurance.presentation.screens.carSearch.CarSearchViewModel
import com.jpapplications.carinsurance.presentation.screens.driverInformation.DriverInformationViewModel
import com.jpapplications.carinsurance.presentation.screens.recommendation.RecommendationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    viewModel { (carRecommendation: CarRecommendation) ->
        RecommendationViewModel(
            recommendation = carRecommendation,
        )
    }

    viewModel { (etxCode: Int) ->
        DriverInformationViewModel(
            etxCode = etxCode,
            getVehicleModelUseCase = get(),
            getOfferConfigurationUseCase = get(),
            getCarRecommendationUseCase = get(),
            getCityByPostalCodeUseCase = get(),
        )
    }
    viewModelOf(::CarSearchViewModel)
    single<CarInsuranceService> { CarInsuranceService(get()) }
    single<CarRecommendationRepository> { CarRecommendationRepositoryImpl(get()) }
    single<CityRepository> { CityRepositoryImpl(get()) }
    single<CarSearchRepository> { CarSearchRepositoryImpl(get()) }
    single<VehicleModelRepository> { VehicleModelRepositoryImpl(get()) }
    single<OfferConfigurationRepository> { OfferConfigurationRepositoryImpl(get()) }
    factory { SearchCarsUseCase(get()) }
    factory { GetVehicleModelUseCase(get()) }
    factory { GetOfferConfigurationUseCase(get()) }
    factory { GetCarRecommendationUseCase(get()) }
    factory { GetCityByPostalCodeUseCase(get()) }
    single { provideHttpClient() }
}