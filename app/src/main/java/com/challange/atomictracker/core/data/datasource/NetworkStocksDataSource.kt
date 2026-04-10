package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.EchoWebSocketSession
import com.challange.atomictracker.core.data.ws.PostmanEchoWebSocketClient
import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.HARDCODED_STOCKS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NetworkStocksDataSource @Inject constructor(
    private val echoWebSocketClient: PostmanEchoWebSocketClient,
    private val json: Json
) : StocksDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _stocks = MutableStateFlow(HARDCODED_STOCKS)
    private val _isConnected = MutableStateFlow(false)

    init {
        scope.launch {
            echoWebSocketClient.connect(
                onConnectionChange = { connected -> _isConnected.value = connected },
            ) { session ->
                tickerLoop(session)
            }
        }
    }

    override fun observeStocks(): Flow<List<StockDto>> = _stocks.asStateFlow()

    override fun observeStock(symbol: String): Flow<StockDto> =
        _stocks
            .map { stocks -> stocks.find { it.symbol == symbol } ?: StockDto(symbol, 0.0, 0.0) }
            .distinctUntilChanged()

    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    /**
     * One WebSocket round-trip per cycle: all symbols in a single JSON array, echo, merge, then wait 2s.
     */
    private suspend fun tickerLoop(session: EchoWebSocketSession) {
        val listSerializer = ListSerializer(StockDto.serializer())
        while (currentCoroutineContext().isActive && _isConnected.value) {
            val batch = HARDCODED_STOCKS.map { stock ->
                StockDto(
                    symbol = stock.symbol,
                    price = Random.nextDouble(20.0, 2000.0),
                    change = Random.nextDouble(-25.0, 25.0),
                )
            }
            val payload = json.encodeToString(listSerializer, batch)
            val echoed = session.sendTextAwaitEcho(payload, ECHO_TIMEOUT_MS) ?: return
            runCatching {
                val ticks = json.decodeFromString(listSerializer, echoed)
                _stocks.value = ticks
            }
            delay(TICK_CYCLE_DELAY_MS)
        }
    }

    private companion object {
        const val TICK_CYCLE_DELAY_MS = 2_000L
        const val ECHO_TIMEOUT_MS = 15_000L
    }
}
