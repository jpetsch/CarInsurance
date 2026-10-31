package com.jpapplications.carinsurance.di

import com.jpapplications.carinsurance.data.datasource.CarSearchDataSource
import com.jpapplications.carinsurance.data.datasource.MockCarSearchDataSource
import com.jpapplications.carinsurance.data.repository.CarSearchRepositoryImpl
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import com.jpapplications.carinsurance.presentation.screens.CarSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CarSearchViewModel)
    single<CarSearchDataSource> { MockCarSearchDataSource() }
    single<CarSearchRepository> { CarSearchRepositoryImpl(get()) }
    factory { SearchCarsUseCase(get()) }
}