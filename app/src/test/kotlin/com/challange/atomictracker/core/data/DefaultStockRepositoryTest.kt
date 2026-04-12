package com.challange.atomictracker.core.data

import app.cash.turbine.test
import com.challange.atomictracker.core.data.datasource.FakeDataSource
import com.challange.atomictracker.core.data.mapper.StockMapper
import com.challange.atomictracker.core.domain.model.HARDCODED_STOCKS
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStockRepositoryTest {

    private val mapper = StockMapper()

    @Test
    fun observeStocksList_mapsDtosToDomain() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)

        repo.observeStocksList().test {
            assertEquals(mapper.toDomain(HARDCODED_STOCKS), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun observeStock_mapsMatchingSymbol() = runTest {
        val repo = DefaultStockRepository(FakeDataSource(), mapper)
        val dto = HARDCODED_STOCKS.first { it.symbol == "AAPL" }

        repo.observeStock("AAPL").test {
            assertEquals(mapper.toDomain(dto), awaitItem())
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
