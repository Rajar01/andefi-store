package store.andefi.ui.product.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import store.andefi.data.repository.ProductRepository
import store.andefi.ui.common.navigation.ProductDetailsRoute
import store.andefi.ui.product.state.ProductDetailsUiState
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ProductDetailsRoute>()

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

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
}