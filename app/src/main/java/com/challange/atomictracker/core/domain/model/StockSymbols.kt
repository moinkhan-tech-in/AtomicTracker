package com.challange.atomictracker.core.domain.model

import com.challange.atomictracker.core.data.ws.StockDto

/**
 * Prices and changes are fixed demo values, not live quotes.
 */
val HARDCODED_STOCKS: List<StockDto> = listOf(
    StockDto("AAPL", 189.52, 1.35, "Apple Inc."),
    StockDto("GOOG", 142.18, -0.82, "Alphabet Inc."),
    StockDto("TSLA", 248.30, 4.10, "Tesla, Inc."),
    StockDto("AMZN", 178.95, 0.45, "Amazon.com, Inc."),
    StockDto("MSFT", 415.20, 2.05, "Microsoft Corporation"),
    StockDto("NVDA", 892.10, -12.40, "NVIDIA Corporation"),
    StockDto("META", 505.60, 3.25, "Meta Platforms, Inc."),
    StockDto("NFLX", 685.40, -5.15, "Netflix, Inc."),
    StockDto("AMD", 152.75, 2.90, "Advanced Micro Devices"),
    StockDto("INTC", 36.42, -0.33, "Intel Corporation"),
    StockDto("ORCL", 125.80, 0.72, "Oracle Corporation"),
    StockDto("CRM", 268.15, -1.20, "Salesforce, Inc."),
    StockDto("ADBE", 558.00, 4.55, "Adobe Inc."),
    StockDto("CSCO", 48.26, 0.11, "Cisco Systems, Inc."),
    StockDto("IBM", 188.90, -0.95, "IBM Corporation"),
    StockDto("QCOM", 162.34, 1.08, "Qualcomm Incorporated"),
    StockDto("AVGO", 1248.50, 9.75, "Broadcom Inc."),
    StockDto("TXN", 175.60, -0.48, "Texas Instruments Inc."),
    StockDto("AMAT", 152.22, 0.66, "Applied Materials, Inc."),
    StockDto("NOW", 875.40, -7.30, "ServiceNow, Inc."),
    StockDto("PANW", 312.18, 2.44, "Palo Alto Networks, Inc."),
    StockDto("SHOP", 78.55, -1.02, "Shopify Inc."),
    StockDto("UBER", 72.30, 0.88, "Uber Technologies, Inc."),
    StockDto("COIN", 198.75, -4.60, "Coinbase Global, Inc."),
    StockDto("SQ", 68.40, 0.52, "Block, Inc."),
)
