package com.challange.atomictracker.data.datasource

import com.challange.atomictracker.data.ws.StockDto
import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.flow.Flow

interface StocksDataSource {

    fun observeStocks(): Flow<List<StockDto>>

    fun observeStock(symbol: String): Flow<StockDto?>

    val liveFeedConnectionState: Flow<LiveFeedConnectionState>

    fun setLiveFeedEnabled(enabled: Boolean)
}
