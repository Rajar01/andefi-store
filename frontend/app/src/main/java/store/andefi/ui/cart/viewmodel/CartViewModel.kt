package store.andefi.ui.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.CartItemResponseDto
import store.andefi.data.remote.dto.CartItemUpdateQuantityRequestDto
import store.andefi.data.repository.CartRepository
import store.andefi.ui.cart.state.CartUiState
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun onCartItemCheckedChange(cartItemResponseDto: CartItemResponseDto) {
        if (_uiState.value is CartUiState.Success) {
            val cartItemSelected = (_uiState.value as CartUiState.Success)
                .cartItemsChecked.toMutableList()

            if (cartItemSelected.contains(cartItemResponseDto)) {
                cartItemSelected.remove(cartItemResponseDto)
            } else {
                cartItemSelected.add(cartItemResponseDto)
            }

            _uiState.value = (_uiState.value as CartUiState.Success)
                .copy(cartItemsChecked = cartItemSelected)
        }
    }

    fun onCartItemCheckedAllChange() {
        if (_uiState.value is CartUiState.Success) {
            val cartItemSelected = (_uiState.value as CartUiState.Success)
                .cartItemsChecked.toMutableList()

            cartItemSelected.clear()
            cartItemSelected.addAll((_uiState.value as CartUiState.Success).cart.cartItems)

            _uiState.value = (_uiState.value as CartUiState.Success)
                .copy(cartItemsChecked = cartItemSelected)
        }
    }

    fun onCartItemUnCheckedAllChange() {
        if (_uiState.value is CartUiState.Success) {
            val cartItemSelected = (_uiState.value as CartUiState.Success)
                .cartItemsChecked.toMutableList()

            cartItemSelected.clear()

            _uiState.value = (_uiState.value as CartUiState.Success)
                .copy(cartItemsChecked = cartItemSelected)
        }
    }

    fun onRemoveCartItemFromCartConfirmationDialogDismissRequest() {
        if (_uiState.value is CartUiState.Success) {
            _uiState.value = (_uiState.value as CartUiState.Success).copy(
                showRemoveCartItemFromCartConfirmationDialog = false,
                cartItemToBeDeleted = null
            )
        }
    }

    fun onRemoveCartItemFromCartConfirmationDialogShowRequest(cartItemResponseDto: CartItemResponseDto) {
        if (_uiState.value is CartUiState.Success) {
            _uiState.value = (_uiState.value as CartUiState.Success).copy(
                showRemoveCartItemFromCartConfirmationDialog = true,
                cartItemToBeDeleted = cartItemResponseDto
            )
        }
    }

    fun getCart(token: String) {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading

            cartRepository.getCart(token)
                .onSuccess {
                    if (it.cartItems.isEmpty()) {
                        _uiState.value = CartUiState.Empty
                    } else {
                        _uiState.value = CartUiState.Success(cart = it)
                    }
                }
                .onFailure { exception ->
                    _uiState.value = CartUiState.Error
                }
        }
    }

    fun increaseCartItemQuantity(token: String, cartItemResponseDto: CartItemResponseDto) {
        viewModelScope.launch {
            val cartItemUpdateQuantityRequestDto =
                CartItemUpdateQuantityRequestDto(
                    cartItemResponseDto.product.id,
                    cartItemResponseDto.quantity + 1
                )

            cartRepository.updateCartItemQuantityInCart(token, cartItemUpdateQuantityRequestDto)
                .onSuccess {
                    if (_uiState.value is CartUiState.Success) {
                        val cartItems = (_uiState.value as CartUiState.Success).cart.cartItems.map {
                            if (it == cartItemResponseDto) {
                                it.copy(quantity = it.quantity + 1)
                            } else {
                                it
                            }
                        }.toMutableList()

                        val cart = (_uiState.value as CartUiState.Success)
                            .cart.copy(cartItems = cartItems)

                        _uiState.value = (_uiState.value as CartUiState.Success).copy(cart = cart)
                    }
                }
                .onFailure {
                    _snackbarEvent.send("Gagal menambahkan jumlah produk.")
                }
        }
    }

    fun decreaseCartItemQuantity(token: String, cartItemResponseDto: CartItemResponseDto) {
        viewModelScope.launch {
            val cartItemUpdateQuantityRequestDto =
                CartItemUpdateQuantityRequestDto(
                    cartItemResponseDto.product.id,
                    cartItemResponseDto.quantity - 1
                )

            cartRepository.updateCartItemQuantityInCart(token, cartItemUpdateQuantityRequestDto)
                .onSuccess {
                    if (_uiState.value is CartUiState.Success) {
                        val cartItems = (_uiState.value as CartUiState.Success).cart.cartItems.map {
                            if (it == cartItemResponseDto) {
                                it.copy(quantity = it.quantity - 1)
                            } else {
                                it
                            }
                        }.toMutableList()

                        val cart = (_uiState.value as CartUiState.Success)
                            .cart.copy(cartItems = cartItems)

                        _uiState.value = (_uiState.value as CartUiState.Success).copy(cart = cart)
                    }
                }
                .onFailure {
                    _snackbarEvent.send("Gagal mengurangi jumlah produk.")
                }
        }
    }

    fun removeProductFromCart(token: String, cartItemResponseDto: CartItemResponseDto) {
        viewModelScope.launch {
            cartRepository.removeProductFromCart(token, cartItemResponseDto.product.id)
                .onSuccess {
                    if (_uiState.value is CartUiState.Success) {
                        val cartItems =
                            (_uiState.value as CartUiState.Success).cart.cartItems.filter { it != cartItemResponseDto }
                                .toMutableList()

                        val cart = (_uiState.value as CartUiState.Success)
                            .cart.copy(cartItems = cartItems)

                        _uiState.value = (_uiState.value as CartUiState.Success).copy(
                            cart = cart,
                            cartItemToBeDeleted = null
                        )

                        // Set cart ui state to empty when cart item is empty after delete
                        if ((_uiState.value as CartUiState.Success).cart.cartItems.isEmpty()) {
                            _uiState.value = CartUiState.Empty
                        }
                    }
                }
                .onFailure {
                    _snackbarEvent.send("Gagal menghapus produk dari keranjang.")
                }

            onRemoveCartItemFromCartConfirmationDialogDismissRequest()
        }
    }
}