package com.challange.atomictracker.core.domain.usecase

import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.model.LiveFeedConnectionState
import kotlinx.coroutines.flow.Flow

class GetLiveFeedConnectionStateFlowUseCase(
    private val stockRepository: StockRepository
) {
    operator fun invoke(): Flow<LiveFeedConnectionState> = stockRepository.liveFeedConnectionState
}
