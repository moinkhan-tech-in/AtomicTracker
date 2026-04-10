package com.challange.atomictracker.feature.feed

import com.challange.atomictracker.core.domain.model.Stock
import kotlinx.collections.immutable.ImmutableList

sealed interface FeedUiState {
    data object Loading : FeedUiState

    data class Success(val stocks: ImmutableList<Stock>) : FeedUiState

    data object Empty : FeedUiState

    data class Error(val message: String) : FeedUiState
}
