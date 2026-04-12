package com.challange.atomictracker.core.domain.fakes

import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeStockRepository : StockRepository {

    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    private val _liveFeedConnectionState =
        MutableStateFlow(LiveFeedConnectionState.Disconnected)

    var lastSetLiveFeedEnabled: Boolean? = null
        private set
    var setLiveFeedEnabledCallCount: Int = 0
        private set

    override fun observeStocksList(): Flow<List<Stock>> = _stocks.asStateFlow()

    override fun observeStock(symbol: String): Flow<Stock> =
        _stocks.map { stocks -> stocks.first { it.symbol == symbol } }

    override val liveFeedConnectionState: Flow<LiveFeedConnectionState> =
        _liveFeedConnectionState.asStateFlow()

    override fun setLiveFeedEnabled(enabled: Boolean) {
        lastSetLiveFeedEnabled = enabled
        setLiveFeedEnabledCallCount++
        _liveFeedConnectionState.value = if (enabled) {
            LiveFeedConnectionState.Connected
        } else {
            LiveFeedConnectionState.Disconnected
        }
    }

    fun setStocks(stocks: List<Stock>) {
        _stocks.value = stocks
    }

    fun setLiveFeedConnectionState(state: LiveFeedConnectionState) {
        _liveFeedConnectionState.value = state
    }
}
