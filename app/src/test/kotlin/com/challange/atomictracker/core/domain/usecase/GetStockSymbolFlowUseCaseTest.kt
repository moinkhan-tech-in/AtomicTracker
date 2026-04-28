package com.challange.atomictracker.core.domain.usecase

import app.cash.turbine.test
import com.challange.atomictracker.core.domain.fakes.FakeStockRepository
import com.challange.atomictracker.domain.model.Stock
import com.challange.atomictracker.domain.usecase.GetStockSymbolFlowUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetStockSymbolFlowUseCaseTest {

    @Test
    fun invoke_emitsStockForSymbol() = runTest {
        val aapl = Stock("AAPL", 189.52, 1.35, "Apple Inc.")
        val fake = FakeStockRepository().apply {
            setStocks(listOf(aapl, Stock("MSFT", 415.20, 2.05, "Microsoft Corporation")))
        }
        val useCase = GetStockSymbolFlowUseCase(fake)

        useCase("AAPL").test {
            assertEquals(Result.success(aapl), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
