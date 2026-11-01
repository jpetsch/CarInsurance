package com.jpapplications.carinsurance.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jpapplications.carinsurance.presentation.screens.carSearch.CarSearchScreen
import com.jpapplications.carinsurance.presentation.screens.carSearch.CarSearchViewModel
import com.jpapplications.carinsurance.presentation.screens.driverInformation.DriverInformationScreen
import com.jpapplications.carinsurance.presentation.screens.driverInformation.DriverInformationViewModel
import io.ktor.http.parametersOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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

                        LaunchedEffect(Unit) {
                            viewModel.carModelSelected.collect {
                                backStack.add(Route.DriverInformation(it))
                            }
                        }

                        CarSearchScreen(
                            uiState = uiState,
                            onEvent = onEvent,
                        )
                    }

                    entry<Route.DriverInformation> {
                        val viewModel: DriverInformationViewModel = koinViewModel(
                            parameters = { parametersOf(it.etxCode) }
                        )
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        val onEvent = viewModel::onEvent

                        DriverInformationScreen(
                            uiState = uiState,
                            onEvent = onEvent,
                        )
                    }
                },
        )
    }
}
