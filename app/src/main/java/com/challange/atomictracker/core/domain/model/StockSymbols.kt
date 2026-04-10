package com.challange.atomictracker.core.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Fixed universe of tickers for the price tracker (25 symbols). */
val DEFAULT_STOCK_SYMBOLS: ImmutableList<String> = persistentListOf(
    "AAPL",
    "GOOG",
    "TSLA",
    "AMZN",
    "MSFT",
    "NVDA",
    "META",
    "NFLX",
    "AMD",
    "INTC",
    "ORCL",
    "CRM",
    "ADBE",
    "CSCO",
    "IBM",
    "QCOM",
    "AVGO",
    "TXN",
    "AMAT",
    "NOW",
    "PANW",
    "SHOP",
    "UBER",
    "COIN",
    "SQ",
)
