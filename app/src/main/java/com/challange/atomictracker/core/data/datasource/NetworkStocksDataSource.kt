package com.challange.atomictracker.core.data.datasource

import com.challange.atomictracker.core.data.ws.EchoWebSocketSession
import com.challange.atomictracker.core.data.ws.PostmanEchoWebSocketClient
import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.HARDCODED_STOCKS
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _liveFeedConnectionState = MutableStateFlow(LiveFeedConnectionState.Connecting)
    private var feedConnectionJob: Job? = null

    init {
        startLiveConnection()
    }

    override fun observeStocks(): Flow<List<StockDto>> = _stocks

    override fun observeStock(symbol: String): Flow<StockDto> =
        _stocks
            .map { stocks ->
                stocks.find { it.symbol == symbol }
                    ?: StockDto(
                        symbol = symbol,
                        price = 0.0,
                        change = 0.0,
                        companyName = HARDCODED_STOCKS.find { it.symbol == symbol }?.companyName.orEmpty(),
                    )
            }
            .distinctUntilChanged()

    override val liveFeedConnectionState: StateFlow<LiveFeedConnectionState> = _liveFeedConnectionState

    override fun setLiveFeedEnabled(enabled: Boolean) {
        val wantLive = _liveFeedConnectionState.value != LiveFeedConnectionState.Disconnected
        if (wantLive == enabled) return
        if (!enabled) {
            // Set paused first so ticker stops and late WebSocket callbacks cannot flip us back to Connecting.
            _liveFeedConnectionState.value = LiveFeedConnectionState.Disconnected
            feedConnectionJob?.cancel()
            feedConnectionJob = null
        } else {
            _liveFeedConnectionState.value = LiveFeedConnectionState.Connecting
            startLiveConnection()
        }
    }

    private fun startLiveConnection() {
        if (_liveFeedConnectionState.value == LiveFeedConnectionState.Disconnected) return
        feedConnectionJob?.cancel()
        feedConnectionJob = scope.launch {
            if (_liveFeedConnectionState.value == LiveFeedConnectionState.Disconnected) return@launch
            _liveFeedConnectionState.value = LiveFeedConnectionState.Connecting
            try {
                echoWebSocketClient.connect(
                    onConnectionChange = { connected ->
                        if (_liveFeedConnectionState.value != LiveFeedConnectionState.Disconnected) {
                            _liveFeedConnectionState.value = if (connected) {
                                LiveFeedConnectionState.Connected
                            } else {
                                LiveFeedConnectionState.Connecting
                            }
                        }
                    },
                ) { session ->
                    tickerLoop(session)
                }
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    /**
     * One WebSocket round-trip per cycle: all symbols in a single JSON array, echo, merge, then wait 2s.
     */
    private suspend fun tickerLoop(session: EchoWebSocketSession) {
        val listSerializer = ListSerializer(StockDto.serializer())
        while (currentCoroutineContext().isActive &&
            _liveFeedConnectionState.value != LiveFeedConnectionState.Disconnected
        ) {
            val batch = HARDCODED_STOCKS.map { stock ->
                StockDto(
                    symbol = stock.symbol,
                    price = Random.nextDouble(20.0, 2000.0),
                    change = Random.nextDouble(-25.0, 25.0),
                    companyName = stock.companyName,
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
