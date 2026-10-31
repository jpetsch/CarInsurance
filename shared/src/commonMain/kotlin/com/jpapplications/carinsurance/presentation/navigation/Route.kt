package com.jpapplications.carinsurance.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object CarSearch : Route, NavKey
}
