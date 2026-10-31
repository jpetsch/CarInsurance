package com.jpapplications.carinsurance.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import carinsurance.shared.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import carinsurance.shared.generated.resources.app_name
import com.jpapplications.carinsurance.presentation.screens.CarSearchScreen
import com.jpapplications.carinsurance.presentation.screens.CarSearchViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclassesOfSealed<Route>()
                }
            }
        },
        Route.CarSearch,
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.app_name),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider =
                entryProvider {
                    entry<Route.CarSearch> {
                        val viewModel: CarSearchViewModel = koinViewModel()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        val onEvent = viewModel::onEvent

                        CarSearchScreen(
                            uiState = uiState,
                            onEvent = onEvent,
                        )
                    }
                },
        )
    }
}
