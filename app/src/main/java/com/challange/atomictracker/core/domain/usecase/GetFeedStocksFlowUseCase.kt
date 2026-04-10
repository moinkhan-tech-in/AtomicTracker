package com.challange.atomictracker.core.domain.usecase

import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFeedStocksFlowUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(): Flow<ImmutableList<Stock>> =
        stockRepository.observeStocksList().map { it.toImmutableList() }
}
