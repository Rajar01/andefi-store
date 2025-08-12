package store.andefi.ui.order.state

import store.andefi.data.remote.dto.ShippingAddressResponseDto

sealed class ShippingAddressUiState {
    data object Loading : ShippingAddressUiState()

    data class Success(
        val shippingAddressResponseDto: ShippingAddressResponseDto,
    ) : ShippingAddressUiState()

    data object Error : ShippingAddressUiState()
}