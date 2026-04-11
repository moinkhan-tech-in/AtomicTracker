package com.challange.atomictracker.core.domain.usecase

import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow

class GetStockSymbolFlowUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(symbol: String): Flow<Stock> =
        stockRepository.observeStock(symbol)
}
