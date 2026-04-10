package com.challange.atomictracker.core.domain.model

import com.challange.atomictracker.core.data.ws.StockDto
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Prices and changes are fixed demo values, not live quotes.
 */
val HARDCODED_STOCKS: List<StockDto> = listOf(
    StockDto("AAPL", 189.52, 1.35),
    StockDto("GOOG", 142.18, -0.82),
    StockDto("TSLA", 248.30, 4.10),
    StockDto("AMZN", 178.95, 0.45),
    StockDto("MSFT", 415.20, 2.05),
    StockDto("NVDA", 892.10, -12.40),
    StockDto("META", 505.60, 3.25),
    StockDto("NFLX", 685.40, -5.15),
    StockDto("AMD", 152.75, 2.90),
    StockDto("INTC", 36.42, -0.33),
    StockDto("ORCL", 125.80, 0.72),
    StockDto("CRM", 268.15, -1.20),
    StockDto("ADBE", 558.00, 4.55),
    StockDto("CSCO", 48.26, 0.11),
    StockDto("IBM", 188.90, -0.95),
    StockDto("QCOM", 162.34, 1.08),
    StockDto("AVGO", 1248.50, 9.75),
    StockDto("TXN", 175.60, -0.48),
    StockDto("AMAT", 152.22, 0.66),
    StockDto("NOW", 875.40, -7.30),
    StockDto("PANW", 312.18, 2.44),
    StockDto("SHOP", 78.55, -1.02),
    StockDto("UBER", 72.30, 0.88),
    StockDto("COIN", 198.75, -4.60),
    StockDto("SQ", 68.40, 0.52),
)