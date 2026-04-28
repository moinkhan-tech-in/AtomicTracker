package com.challange.atomictracker.domain.usecase

import com.challange.atomictracker.domain.repository.StockRepository
import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.flow.Flow

class GetLiveFeedConnectionStateFlowUseCase(
    private val stockRepository: StockRepository
) {
    operator fun invoke(): Flow<LiveFeedConnectionState> = stockRepository.liveFeedConnectionState
}
