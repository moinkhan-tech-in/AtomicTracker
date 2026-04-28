package com.challange.atomictracker.domain.usecase

import com.challange.atomictracker.domain.repository.StockRepository
import com.challange.atomictracker.domain.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class GetStockSymbolFlowUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(symbol: String): Flow<Result<Stock>> =
        stockRepository.observeStock(symbol).catch { emit(Result.failure(it)) }
}
