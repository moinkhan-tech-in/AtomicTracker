package com.challange.atomictracker.core.domain.model

data class Stock(
    val symbol: String,
    val price: Double,
    val change: Double
) {
    val isPositive: Boolean
        get() = change > 0

    val isNegative: Boolean
        get() = change < 0

    fun priceDirection(): PriceDirection = when {
        isPositive -> PriceDirection.Up
        isNegative -> PriceDirection.Down
        else -> PriceDirection.Neutral
    }
}