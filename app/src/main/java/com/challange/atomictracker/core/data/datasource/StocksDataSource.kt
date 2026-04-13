package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.flow.Flow

interface StocksDataSource {

    fun observeStocks(): Flow<List<StockDto>>

    fun observeStock(symbol: String): Flow<StockDto?>

    val liveFeedConnectionState: Flow<LiveFeedConnectionState>

    fun setLiveFeedEnabled(enabled: Boolean)
}
