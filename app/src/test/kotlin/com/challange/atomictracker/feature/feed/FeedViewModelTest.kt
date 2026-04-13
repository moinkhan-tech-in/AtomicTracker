package com.challange.atomictracker.feature.feed

import app.cash.turbine.test
import com.challange.atomictracker.MainDispatcherRule
import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import com.challange.atomictracker.core.domain.usecase.GetFeedStocksFlowUseCase
import com.challange.atomictracker.core.domain.usecase.GetLiveFeedConnectionStateFlowUseCase
import com.challange.atomictracker.core.domain.usecase.SetLiveFeedEnabledUseCase
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

class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun uiState_emitsLoadingThenSuccess() = runTest {
        val stocks = listOf(Stock("AAPL", 1.0, 0.1, "Apple Inc."))
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStocksList() } returns flowOf(Result.success(stocks))
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = createViewModel(repo)

        viewModel.uiState.test {
            assertEquals(FeedUiState.Loading, awaitItem())
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            val success = awaitItem() as FeedUiState.Success
            assertEquals(stocks, success.stocks.toList())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun uiState_emitsError_whenUpstreamFails() = runTest {
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStocksList() } returns flow { throw IOException("network down") }
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = createViewModel(repo)

        viewModel.uiState.test {
            assertEquals(FeedUiState.Loading, awaitItem())
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            val error = awaitItem() as FeedUiState.Error
            assertEquals("network down", error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun liveFeedConnectionState_startsConnectingThenFollowsUseCase() = runTest {
        val liveFlow = MutableStateFlow(LiveFeedConnectionState.Disconnected)
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStocksList() } returns flowOf(Result.success(emptyList()))
            every { liveFeedConnectionState } returns liveFlow.asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = createViewModel(repo)

        viewModel.liveFeedConnectionState.test {
            assertEquals(LiveFeedConnectionState.Connecting, awaitItem())
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(LiveFeedConnectionState.Disconnected, awaitItem())
            liveFlow.value = LiveFeedConnectionState.Connected
            mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(LiveFeedConnectionState.Connected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setLiveFeedEnabled_delegatesToUseCase() = runTest {
        val repo = mockk<StockRepository>(relaxed = true) {
            every { observeStocksList() } returns flowOf(Result.success(emptyList()))
            every { liveFeedConnectionState } returns
                MutableStateFlow(LiveFeedConnectionState.Disconnected).asStateFlow()
            every { setLiveFeedEnabled(any()) } just Runs
        }
        val viewModel = createViewModel(repo)

        viewModel.setLiveFeedEnabled(true)
        viewModel.setLiveFeedEnabled(false)

        verify(exactly = 1) { repo.setLiveFeedEnabled(true) }
        verify(exactly = 1) { repo.setLiveFeedEnabled(false) }
    }

    private fun createViewModel(repo: StockRepository): FeedViewModel {
        val getFeed = GetFeedStocksFlowUseCase(repo)
        val liveFeed = GetLiveFeedConnectionStateFlowUseCase(repo)
        val setLive = SetLiveFeedEnabledUseCase(repo)
        return FeedViewModel(getFeed, liveFeed, setLive)
    }
}
