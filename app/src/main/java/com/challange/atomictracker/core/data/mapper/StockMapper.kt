package com.challange.atomictracker.core.data.mapper

import com.challange.atomictracker.core.data.ws.StockDto
import com.challange.atomictracker.core.domain.model.Stock
import javax.inject.Inject

class StockMapper @Inject constructor() {

    fun toDomain(dto: StockDto): Stock =
        Stock(
            symbol = dto.symbol,
            price = dto.price,
            change = dto.change,
        )

    fun toDomain(dtos: List<StockDto>): List<Stock> = dtos.map { toDomain(it) }
}
