package com.challange.atomictracker.presentation.feed

import com.challange.atomictracker.domain.model.Stock
import kotlinx.collections.immutable.ImmutableList

sealed interface FeedUiState {
    data object Loading : FeedUiState

    data class Success(val stocks: ImmutableList<Stock>) : FeedUiState

    data object Empty : FeedUiState

    data class Error(val message: String) : FeedUiState
}
