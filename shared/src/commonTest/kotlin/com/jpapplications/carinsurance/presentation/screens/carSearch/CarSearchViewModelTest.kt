package com.jpapplications.carinsurance.presentation.screens.carSearch

import com.jpapplications.carinsurance.domain.carFixture
import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import com.jpapplications.carinsurance.domain.model.CarSearchResult
import com.jpapplications.carinsurance.domain.model.NetworkError
import com.jpapplications.carinsurance.domain.repository.CarSearchRepository
import com.jpapplications.carinsurance.domain.usecase.SearchCarsUseCase
import com.jpapplications.carinsurance.presentation.runViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CarSearchViewModelTest {
    @Test
    fun searchIsDebouncedAndUpdatesGroupedResults() = runViewModelTest {
        val repository = RecordingCarSearchRepository()
        val viewModel = CarSearchViewModel(SearchCarsUseCase(repository))

        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.YearChanged("2024"))
        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.SearchTermChanged("BMW"))
        runCurrent()
        advanceTimeBy(499)
        runCurrent()

        assertTrue(repository.criteria.isEmpty())
        advanceTimeBy(1)
        runCurrent()

        assertEquals(listOf(CarSearchCriteria("BMW", 2024)), repository.criteria)
        assertEquals(listOf(carFixture()), viewModel.uiState.value.carGroups["BMW"])
        assertNull(viewModel.uiState.value.searchError)
        assertFalse(viewModel.uiState.value.hasYearError)
    }

    @Test
    fun invalidYearAndShortSearchTermDoNotCallRepository() = runViewModelTest {
        val repository = RecordingCarSearchRepository()
        val viewModel = CarSearchViewModel(SearchCarsUseCase(repository))

        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.YearChanged("1974"))
        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.SearchTermChanged("B"))
        runCurrent()
        advanceTimeBy(600)
        runCurrent()

        assertTrue(viewModel.uiState.value.hasYearError)
        assertTrue(repository.criteria.isEmpty())
    }

    @Test
    fun selectingGroupClearsCarAndFailureIsShown() = runViewModelTest {
        val repository =
            RecordingCarSearchRepository(
                result = CarSearchResult.Failure(NetworkError.SERVER),
            )
        val viewModel = CarSearchViewModel(SearchCarsUseCase(repository))

        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.SearchTermChanged("BMW"))
        runCurrent()
        advanceTimeBy(500)
        runCurrent()
        val group = mapOf("BMW" to listOf(carFixture())).entries.first()

        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.CarSelected(group))

        assertEquals(group, viewModel.uiState.value.selectedCarGroup)
        assertNull(viewModel.uiState.value.selectedCar)
        assertEquals(NetworkError.SERVER, viewModel.uiState.value.searchError)
        assertTrue(viewModel.uiState.value.carGroups.isEmpty())
    }

    @Test
    fun selectingCarEntryEmitsItsCode() = runViewModelTest {
        val viewModel = CarSearchViewModel(SearchCarsUseCase(RecordingCarSearchRepository()))
        val selected = mutableListOf<Int>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.carModelSelected.collect(selected::add)
        }

        viewModel.onEvent(CarSearchViewModel.CarSearchEvent.CarEntrySelected(9876))
        runCurrent()

        assertEquals(listOf(9876), selected)
    }

    private class RecordingCarSearchRepository(
        private val result: CarSearchResult =
            CarSearchResult.Success(mapOf("BMW" to listOf(carFixture()))),
    ) : CarSearchRepository {
        val criteria = mutableListOf<CarSearchCriteria>()

        override suspend fun searchCars(criteria: CarSearchCriteria): CarSearchResult {
            this.criteria += criteria
            return result
        }
    }
}
