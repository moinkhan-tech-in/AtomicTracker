package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.StockDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StocksDataSource {

    fun observeStocks(): Flow<List<StockDto>>

    fun observeStock(symbol: String): Flow<StockDto>

    val isConnected: StateFlow<Boolean>
}
