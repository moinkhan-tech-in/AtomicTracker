package com.challange.atomictracker.data

import com.challange.atomictracker.data.datasource.StocksDataSource
import com.challange.atomictracker.data.mapper.StockMapper
import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.domain.model.Stock
import com.challange.atomictracker.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultStockRepository @Inject constructor(
    private val stocksDataSource: StocksDataSource,
    private val stockMapper: StockMapper,
) : StockRepository {

    override fun observeStocksList(): Flow<Result<List<Stock>>> =
        stocksDataSource.observeStocks().map { dtos ->
            runCatching { stockMapper.toDomain(dtos) }
        }

    override fun observeStock(symbol: String): Flow<Result<Stock>> =
        stocksDataSource.observeStock(symbol).map { dto ->
            runCatching {
                if (dto == null) {
                    throw IllegalArgumentException("Unknown symbol: $symbol")
                } else {
                    stockMapper.toDomain(dto)
                }
            }
        }

    override val liveFeedConnectionState: Flow<LiveFeedConnectionState> =
        stocksDataSource.liveFeedConnectionState

    override fun setLiveFeedEnabled(enabled: Boolean) {
        stocksDataSource.setLiveFeedEnabled(enabled)
    }
}
