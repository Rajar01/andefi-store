package store.andefi.ui.order.state

import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.remote.dto.ShippingAddressResponseDto

sealed class CheckoutUiState {
    data object Loading : CheckoutUiState()

    data class Success(
        val shippingAddressResponseDto: ShippingAddressResponseDto,
        val products: List<ProductResponseDto>,
        val orderCheckoutRequestDto: OrderCheckoutRequestDto
    ) : CheckoutUiState()

    data object Error : CheckoutUiState()
}