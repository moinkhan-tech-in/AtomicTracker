package com.challange.atomictracker.data.mapper

import com.challange.atomictracker.data.ws.StockDto
import com.challange.atomictracker.domain.model.Stock
import javax.inject.Inject

class StockMapper @Inject constructor() {

    fun toDomain(dto: StockDto): Stock =
        Stock(
            symbol = dto.symbol,
            price = dto.price,
            change = dto.change,
            companyName = dto.companyName,
        )

    fun toDomain(dtos: List<StockDto>): List<Stock> = dtos.map { toDomain(it) }
}
