package com.challange.atomictracker.domain.usecase

import com.challange.atomictracker.domain.repository.StockRepository
import com.challange.atomictracker.domain.model.Stock
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetFeedStocksFlowUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(): Flow<Result<ImmutableList<Stock>>> =
        stockRepository.observeStocksList().map { result ->
            result.map { stocks ->
                stocks
                    .sortedByDescending(Stock::price)
                    .toImmutableList()
            }
        }.catch { emit(Result.failure(it)) }
}
