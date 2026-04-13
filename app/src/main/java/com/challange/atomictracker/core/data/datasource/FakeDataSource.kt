package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.HARDCODED_STOCKS
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeDataSource @Inject constructor() : StocksDataSource {

    private val _liveFeedConnectionState =
        MutableStateFlow(LiveFeedConnectionState.Connected)

    override val liveFeedConnectionState: StateFlow<LiveFeedConnectionState> =
        _liveFeedConnectionState.asStateFlow()

    override fun setLiveFeedEnabled(enabled: Boolean) {
        val wantLive = _liveFeedConnectionState.value != LiveFeedConnectionState.Disconnected
        if (wantLive == enabled) return
        _liveFeedConnectionState.value = if (enabled) {
            LiveFeedConnectionState.Connected
        } else {
            LiveFeedConnectionState.Disconnected
        }
    }

    override fun observeStocks(): Flow<List<StockDto>> = flow {
        emit(HARDCODED_STOCKS)
    }

    override fun observeStock(symbol: String): Flow<StockDto?> =
        observeStocks().map { stocks -> stocks.firstOrNull { it.symbol == symbol } }
}
