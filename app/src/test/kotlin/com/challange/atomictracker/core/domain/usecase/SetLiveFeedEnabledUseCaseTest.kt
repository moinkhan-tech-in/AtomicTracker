package com.challange.atomictracker.core.domain.usecase

import com.challange.atomictracker.core.domain.fakes.FakeStockRepository
import com.challange.atomictracker.domain.usecase.SetLiveFeedEnabledUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class SetLiveFeedEnabledUseCaseTest {

    @Test
    fun invoke_forwardsToRepository() {
        val fake = FakeStockRepository()
        val useCase = SetLiveFeedEnabledUseCase(fake)

        useCase(true)
        assertEquals(true, fake.lastSetLiveFeedEnabled)
        assertEquals(1, fake.setLiveFeedEnabledCallCount)

        useCase(false)
        assertEquals(false, fake.lastSetLiveFeedEnabled)
        assertEquals(2, fake.setLiveFeedEnabledCallCount)
    }
}
