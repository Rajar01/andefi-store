package store.andefi.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.ReviewResponseDto
import store.andefi.data.repository.ProductRepository
import store.andefi.ui.product.state.ProductReviewAndRatingUiState
import javax.inject.Inject

@HiltViewModel
class ProductReviewAndRatingViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductReviewAndRatingUiState())
    val uiState: StateFlow<ProductReviewAndRatingUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()
    fun onRatingBottomSheetDismissRequest() {
        _uiState.value = _uiState.value.copy(showRatingBottomSheet = false)
    }

    fun onSortBottomSheetDismissRequest() {
        _uiState.value = _uiState.value.copy(showSortBottomSheet = false)
    }

    fun onRatingBottomSheetShowRequest() {
        _uiState.value = _uiState.value.copy(showRatingBottomSheet = true)
    }

    fun onSortBottomSheetShowRequest() {
        _uiState.value = _uiState.value.copy(showSortBottomSheet = true)
    }

    fun onRatingBottomSheetCheckedChange(value: String?) {
        _uiState.value = _uiState.value.copy(ratingFilter = value)
    }

    fun onSortBottomSheetCheckedChange(value: String) {
        _uiState.value = _uiState.value.copy(sortFilter = value)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getProductReviews(
        productId: String,
    ): Flow<PagingData<ReviewResponseDto>> {
        return combine(
            _uiState.map { it.ratingFilter },
            _uiState.map { it.sortFilter } // your second value
        ) { ratingFilter, sortFilter ->
            Pair(ratingFilter, sortFilter)
        }
            .debounce { 1000 }.distinctUntilChanged()
            .flatMapLatest { (ratingFilter, sortFilter) ->
                if (!ratingFilter.isNullOrBlank()) productRepository.getProductReviews(
                    productId = productId,
                    rating = ratingFilter,
                    sortBy = sortFilter
                ) else productRepository.getProductReviews(
                    productId = productId,
                    sortBy = sortFilter
                )
            }
            .cachedIn(viewModelScope)
    }

    fun onLoadMoreProductReviewError() {
        viewModelScope.launch {
            _snackbarEvent.send("Maaf, tidak dapat memuat rating dan ulasan lainnya saat ini. Silakan coba beberapa saat lagi.")
        }
    }
}