package com.challange.atomictracker.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import app.cash.turbine.test
import com.challange.atomictracker.MainDispatcherRule
import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import com.challange.atomictracker.core.domain.usecase.GetStockSymbolFlowUseCase
import com.challange.atomictracker.core.navigation.DetailRoute
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], manifest = Config.NONE)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun uiState_emitsLoadingThenSuccess() = runTest {
        val symbol = "AAPL"
        val stock = Stock(symbol, 189.52, 1.35, "Apple Inc.")
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStock(symbol) } returns flowOf(stock)
            every { observeStocksList() } returns flowOf(emptyList())
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = DetailViewModel(savedStateHandleFor(symbol), GetStockSymbolFlowUseCase(repo))

        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(DetailUiState.Success(stock), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun uiState_emitsError_whenUpstreamFails() = runTest {
        val symbol = "AAPL"
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStock(symbol) } returns flow { throw IOException() }
            every { observeStocksList() } returns flowOf(emptyList())
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = DetailViewModel(savedStateHandleFor(symbol), GetStockSymbolFlowUseCase(repo))

        viewModel.uiState.test {
            assertEquals(DetailUiState.Loading, awaitItem())
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            val error = awaitItem() as DetailUiState.Error
            assertEquals("Could not load $symbol", error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeStock_invokedWithRouteSymbol() = runTest {
        val symbol = "NVDA"
        val stock = Stock(symbol, 1.0, 0.0, "NVIDIA")
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStock(symbol) } returns flowOf(stock)
            every { observeStocksList() } returns flowOf(emptyList())
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = DetailViewModel(savedStateHandleFor(symbol), GetStockSymbolFlowUseCase(repo))

        viewModel.uiState.test {
            awaitItem()
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { repo.observeStock(symbol) }
    }

    private fun savedStateHandleFor(symbol: String): SavedStateHandle =
        SavedStateHandle(DetailRoute(symbol = symbol))
}
