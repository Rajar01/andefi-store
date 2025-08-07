package store.andefi.ui.cart.state

import store.andefi.data.remote.dto.CartItemResponseDto
import store.andefi.data.remote.dto.CartResponseDto

sealed class CartUiState {
    data object Loading : CartUiState()
    data class Success(
        val cart: CartResponseDto,
        val cartItemsChecked: List<CartItemResponseDto> = emptyList(),
        val showRemoveCartItemFromCartConfirmationDialog: Boolean = false,
        val cartItemToBeDeleted: CartItemResponseDto? = null
    ) : CartUiState()

    data object Error : CartUiState()
    data object Empty : CartUiState()
}