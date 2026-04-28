package com.challange.atomictracker.core.domain.usecase

import app.cash.turbine.test
import com.challange.atomictracker.core.domain.fakes.FakeStockRepository
import com.challange.atomictracker.domain.model.Stock
import com.challange.atomictracker.domain.usecase.GetFeedStocksFlowUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFeedStocksFlowUseCaseTest {

    @Test
    fun invoke_emitsStocksSortedByPriceDescending() = runTest {
        val stocks = listOf(
            Stock("AAPL", 1.0, 0.1, "Apple Inc."),
            Stock("MSFT", 2.0, -0.2, "Microsoft Corporation"),
        )
        val fake = FakeStockRepository().apply { setStocks(stocks) }
        val useCase = GetFeedStocksFlowUseCase(fake)

        useCase().test {
            val expected = listOf(
                Stock("MSFT", 2.0, -0.2, "Microsoft Corporation"),
                Stock("AAPL", 1.0, 0.1, "Apple Inc."),
            ).toImmutableList()
            assertEquals(Result.success(expected), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
