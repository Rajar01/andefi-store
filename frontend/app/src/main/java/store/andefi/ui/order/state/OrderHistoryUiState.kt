package store.andefi.ui.order.state

import store.andefi.data.remote.dto.OrderResponseDto

sealed class OrderHistoryUiState {
    data object Empty : OrderHistoryUiState()
    
    data object Loading : OrderHistoryUiState()

    data class Success(
        val orders: List<OrderResponseDto>,
    ) : OrderHistoryUiState()

    data object Error : OrderHistoryUiState()
}