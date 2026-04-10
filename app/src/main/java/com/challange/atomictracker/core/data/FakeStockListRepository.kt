package com.challange.atomictracker.core.data

import com.challange.atomictracker.core.domain.model.DEFAULT_STOCK_SYMBOLS
import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeStockListRepository @Inject constructor() : StockListRepository {

    override fun observeStocksList(): Flow<List<Stock>> = flow {
        emit(
            DEFAULT_STOCK_SYMBOLS.map { symbol ->
                Stock(
                    symbol = symbol,
                    price = Random.nextDouble(20.0, 2000.0),
                    change = Random.nextDouble(-25.0, 25.0),
                )
            }
                .sortedByDescending { it.price }
                .toImmutableList(),
        )
    }
}
