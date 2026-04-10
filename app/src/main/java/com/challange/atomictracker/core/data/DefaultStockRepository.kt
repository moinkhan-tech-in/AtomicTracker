package com.challange.atomictracker.core.data

import com.challange.atomictracker.core.data.datasource.StocksDataSource
import com.challange.atomictracker.core.data.mapper.StockMapper
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultStockRepository @Inject constructor(
    private val stocksDataSource: StocksDataSource,
    private val stockMapper: StockMapper,
) : StockRepository {

    override fun observeStocksList(): Flow<List<Stock>> =
        stocksDataSource.observeStocks().map { stockMapper.toDomain(it) }

    override fun observeStock(symbol: String): Flow<Stock> =
        stocksDataSource.observeStock(symbol).map { stockMapper.toDomain(it) }

    override val isFeedConnected: StateFlow<Boolean> = stocksDataSource.isConnected
}
