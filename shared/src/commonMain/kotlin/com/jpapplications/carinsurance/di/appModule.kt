package com.jpapplications.carinsurance.di

import com.jpapplications.carinsurance.presentation.screens.CarSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CarSearchViewModel)
}