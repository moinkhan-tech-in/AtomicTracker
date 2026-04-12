package com.challange.atomictracker.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.challange.atomictracker.core.domain.usecase.GetStockSymbolFlowUseCase
import com.challange.atomictracker.core.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeStock: GetStockSymbolFlowUseCase
) : ViewModel() {

    private val symbol: String = savedStateHandle.toRoute<DetailRoute>().symbol

    val uiState: StateFlow<DetailUiState> = observeStock(symbol)
        .map { stock -> DetailUiState.Success(stock) as DetailUiState }
        .catch { t -> emit(DetailUiState.Error(message = t.message.orEmpty())) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState.Loading,
        )
}
