package com.challange.atomictracker.data.ws

import kotlinx.serialization.Serializable

@Serializable
data class StockDto(
    val symbol: String,
    val price: Double,
    val change: Double,
    val companyName: String = "",
)
