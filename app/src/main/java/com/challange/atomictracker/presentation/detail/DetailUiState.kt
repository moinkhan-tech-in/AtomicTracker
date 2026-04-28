package com.challange.atomictracker.presentation.detail

import com.challange.atomictracker.domain.model.Stock

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Success(val stock: Stock) : DetailUiState

    data class Error(val message: String) : DetailUiState
}
