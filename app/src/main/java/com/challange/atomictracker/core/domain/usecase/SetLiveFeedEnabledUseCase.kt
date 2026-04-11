package com.challange.atomictracker.core.domain.usecase

import com.challange.atomictracker.core.data.StockRepository

class SetLiveFeedEnabledUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(enabled: Boolean) {
        stockRepository.setLiveFeedEnabled(enabled)
    }
}