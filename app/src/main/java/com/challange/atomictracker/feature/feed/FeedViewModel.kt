package com.challange.atomictracker.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challange.atomictracker.core.data.StockRepository
import com.challange.atomictracker.core.domain.usecase.GetFeedStocksFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getFeedStocks: GetFeedStocksFlowUseCase,
    stockRepository: StockRepository
) : ViewModel() {

    val isFeedConnected: StateFlow<Boolean> = stockRepository.isFeedConnected

    val uiState: StateFlow<FeedUiState> = getFeedStocks()
        .map { stocks -> FeedUiState.Success(stocks) as FeedUiState }
        .catch { throwable ->
            emit(
                FeedUiState.Error(
                    message = throwable.message.orEmpty().ifBlank { "Could not load stocks" },
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeedUiState.Loading
        )
}
