package com.challange.atomictracker.core.data

import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StockRepository {
    fun observeStocksList(): Flow<List<Stock>>

    fun observeStock(symbol: String): Flow<Stock>

    val isFeedConnected: StateFlow<Boolean>
}
