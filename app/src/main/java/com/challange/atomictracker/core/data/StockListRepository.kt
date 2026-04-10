package com.challange.atomictracker.core.data

import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockListRepository {
    fun observeStocksList(): Flow<List<Stock>>
}
