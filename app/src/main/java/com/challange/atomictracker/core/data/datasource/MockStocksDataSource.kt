package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.HARDCODED_STOCKS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockStocksDataSource @Inject constructor() : StocksDataSource {

    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    override fun observeStocks(): Flow<List<StockDto>> = flow {
        emit(HARDCODED_STOCKS)
    }

    override fun observeStock(symbol: String): Flow<StockDto> =
        observeStocks().map { stocks -> stocks.first { it.symbol == symbol } }
}
