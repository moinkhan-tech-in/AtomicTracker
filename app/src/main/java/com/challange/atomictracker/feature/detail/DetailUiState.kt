package com.challange.atomictracker.feature.detail

import com.challange.atomictracker.core.domain.model.Stock

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Success(val stock: Stock) : DetailUiState

    data class Error(val message: String) : DetailUiState
}
