package com.jpapplications.carinsurance

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.jpapplications.carinsurance.di.appModule
import com.jpapplications.carinsurance.presentation.navigation.NavigationRoot
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
fun App() {
    KoinApplication(configuration = koinConfiguration { modules(appModule) }) {
        MaterialTheme {
            NavigationRoot()
        }
    }
}