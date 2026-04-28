package com.challange.atomictracker.core.data

import app.cash.turbine.test
import com.challange.atomictracker.data.mapper.StockMapper
import com.challange.atomictracker.domain.model.HARDCODED_STOCKS
import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.data.DefaultStockRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStockRepositoryTest {

    private val mapper = StockMapper()

    @Test
    fun observeStocksList_mapsDtosToDomain() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)

        repo.observeStocksList().test {
            assertEquals(Result.success(mapper.toDomain(HARDCODED_STOCKS)), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun observeStock_mapsMatchingSymbol() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)
        val dto = HARDCODED_STOCKS.first { it.symbol == "AAPL" }

        repo.observeStock("AAPL").test {
            assertEquals(Result.success(mapper.toDomain(dto)), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun observeStock_unknownSymbol_emitsFailure() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)

        repo.observeStock("No Symbol").test {
            val item = awaitItem()
            assert(item.isFailure)
            assertEquals((item.exceptionOrNull() as IllegalArgumentException).message, "Unknown symbol: No Symbol")
            awaitComplete()
        }
    }

    @Test
    fun setLiveFeedEnabled_updatesLiveFeedConnectionState() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)

        repo.liveFeedConnectionState.test {
            assertEquals(LiveFeedConnectionState.Connected, awaitItem())
            repo.setLiveFeedEnabled(false)
            assertEquals(LiveFeedConnectionState.Disconnected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
