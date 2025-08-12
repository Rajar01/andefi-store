package store.andefi.ui.order.state

import store.andefi.data.remote.dto.OrderResponseDto

sealed class OrderDetailUiState {
    data object Loading : OrderDetailUiState()

    data class Success(
        val order: OrderResponseDto,
    ) : OrderDetailUiState()

    data object Error : OrderDetailUiState()
}