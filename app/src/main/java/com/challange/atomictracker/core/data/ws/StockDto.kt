package com.challange.atomictracker.core.data.ws

import kotlinx.serialization.Serializable

@Serializable
data class StockDto(
    val symbol: String,
    val price: Double,
    val change: Double,
)