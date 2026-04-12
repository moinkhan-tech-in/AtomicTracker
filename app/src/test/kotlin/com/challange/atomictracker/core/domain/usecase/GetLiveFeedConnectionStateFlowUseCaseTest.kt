package com.challange.atomictracker.core.domain.usecase

import app.cash.turbine.test
import com.challange.atomictracker.core.domain.fakes.FakeStockRepository
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetLiveFeedConnectionStateFlowUseCaseTest {

    @Test
    fun invoke_emitsConnectionStateUpdates() = runTest {
        val fake = FakeStockRepository().apply {
            setLiveFeedConnectionState(LiveFeedConnectionState.Connecting)
        }
        val useCase = GetLiveFeedConnectionStateFlowUseCase(fake)

        useCase().test {
            assertEquals(LiveFeedConnectionState.Connecting, awaitItem())
            fake.setLiveFeedConnectionState(LiveFeedConnectionState.Connected)
            assertEquals(LiveFeedConnectionState.Connected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
