package com.challange.atomictracker.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challange.atomictracker.domain.model.LiveFeedConnectionState
import com.challange.atomictracker.domain.usecase.GetFeedStocksFlowUseCase
import com.challange.atomictracker.domain.usecase.GetLiveFeedConnectionStateFlowUseCase
import com.challange.atomictracker.domain.usecase.SetLiveFeedEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getFeedStocksFlowUseCase: GetFeedStocksFlowUseCase,
    liveFeedFlowUseCase: GetLiveFeedConnectionStateFlowUseCase,
    private val setLiveFeedEnabledUseCase: SetLiveFeedEnabledUseCase
) : ViewModel() {

    val liveFeedConnectionState: StateFlow<LiveFeedConnectionState> =
        liveFeedFlowUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LiveFeedConnectionState.Connecting,
            )

    val uiState: StateFlow<FeedUiState> = getFeedStocksFlowUseCase()
        .map { result ->
            result.fold(
                onSuccess = { stocks ->
                    if (stocks.isEmpty()) FeedUiState.Empty
                    else FeedUiState.Success(stocks)
                },
                onFailure = { t -> FeedUiState.Error(message = t.message.orEmpty()) },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeedUiState.Loading
        )

    fun setLiveFeedEnabled(enabled: Boolean) = setLiveFeedEnabledUseCase(enabled)
}
