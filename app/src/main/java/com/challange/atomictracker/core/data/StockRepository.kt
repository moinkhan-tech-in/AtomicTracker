package com.challange.atomictracker.core.data

import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun observeStocksList(): Flow<List<Stock>>

    fun observeStock(symbol: String): Flow<Stock>

    fun setLiveFeedEnabled(enabled: Boolean)

    val liveFeedConnectionState: Flow<LiveFeedConnectionState>
}
