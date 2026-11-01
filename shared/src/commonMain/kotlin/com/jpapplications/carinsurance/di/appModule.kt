package com.jpapplications.carinsurance.di

import com.jpapplications.carinsurance.data.datasource.CarInsuranceService
import com.jpapplications.carinsurance.data.network.provideHttpClient
import com.jpapplications.carinsurance.data.repository.CarSearchRepositoryImpl
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import com.jpapplications.carinsurance.presentation.screens.carSearch.CarSearchViewModel
import com.jpapplications.carinsurance.presentation.screens.driverInformation.DriverInformationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModel { (etxCode: Int) ->
        DriverInformationViewModel(
            etxCode = etxCode,
        )
    }
    viewModelOf(::CarSearchViewModel)
    single<CarInsuranceService> { CarInsuranceService(get()) }
    single<CarSearchRepository> { CarSearchRepositoryImpl(get()) }
    factory { SearchCarsUseCase(get()) }
    single { provideHttpClient() }
}