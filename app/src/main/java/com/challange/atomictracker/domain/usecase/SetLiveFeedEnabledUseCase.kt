package com.challange.atomictracker.domain.usecase

import com.challange.atomictracker.domain.repository.StockRepository

class SetLiveFeedEnabledUseCase(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(enabled: Boolean) {
        stockRepository.setLiveFeedEnabled(enabled)
    }
}