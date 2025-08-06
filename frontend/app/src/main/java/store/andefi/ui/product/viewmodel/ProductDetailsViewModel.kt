package store.andefi.ui.product.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.repository.CartRepository
import store.andefi.data.repository.ProductRepository
import store.andefi.exception.AuthenticationFailedException
import store.andefi.ui.common.navigation.ProductDetailsRoute
import store.andefi.ui.product.state.ProductDetailsUiState
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ProductDetailsRoute>()

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isLoading = true, isError = false)

            async { getProductById(args.productId) }.await()
            async { getProductReview(args.productId) }.await()
            async { getProductReviewSummary(args.productId) }.await()

            _uiState.value =
                _uiState.value.copy(isLoading = false, isError = false)
        }
    }

    suspend fun getProductById(productId: String) {
        productRepository.getProductById(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    product = it,
                    isError = false,
                )
            }
            .onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
    }

    suspend fun getProductReview(productId: String) {
        productRepository.getProductReview(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    review = it,
                    isError = false,
                )
            }
            .onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
    }

    suspend fun getProductReviewSummary(productId: String) {
        productRepository.getProductReviewSummary(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    reviewSummary = it,
                    isError = false,
                )
            }
            .onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
    }

    fun getProductReviewMedia(productId: String): Flow<PagingData<String>> {
        return productRepository.getProductReviewMedia(productId)
            .cachedIn(viewModelScope)
    }

    fun addProductIntoCart(token: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddProductIntoCartLoading = true)

            cartRepository.addProductIntoCart(token, args.productId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isAddProductIntoCartLoading = false,
                        isAddProductIntoCartSuccessful = true,
                    )

                    _snackbarEvent.send("Produk berhasil ditambahkan ke keranjang.")
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isAddProductIntoCartLoading = false,
                        isAddProductIntoCartSuccessful = false,
                    )

                    when (exception) {
                        is AuthenticationFailedException -> _snackbarEvent.send("Silakan masuk atau daftar untuk menambahkan produk ke keranjang.")
                        else -> _snackbarEvent.send("Gagal menambahkan produk ke keranjang.")
                    }
                }
        }
    }
}