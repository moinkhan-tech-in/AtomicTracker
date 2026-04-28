package com.challange.atomictracker.domain.repository

import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun observeStocksList(): Flow<Result<List<Stock>>>

    fun observeStock(symbol: String): Flow<Result<Stock>>

    fun setLiveFeedEnabled(enabled: Boolean)

    val liveFeedConnectionState: Flow<LiveFeedConnectionState>
}