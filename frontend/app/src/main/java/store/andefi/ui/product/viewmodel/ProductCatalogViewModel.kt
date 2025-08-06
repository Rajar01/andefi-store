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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.repository.ProductRepository
import store.andefi.ui.product.state.ProductCatalogUiState
import javax.inject.Inject

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductCatalogUiState())
    val uiState: StateFlow<ProductCatalogUiState> = _uiState.asStateFlow()
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getProducts(): Flow<PagingData<ProductResponseDto>> {
        return _uiState
            .map { it.query.trim() }
            .debounce { 1000 }.distinctUntilChanged()
            .flatMapLatest { if (it.isNotBlank()) productRepository.getProducts(keyword = it) else productRepository.getProducts() }
            .cachedIn(viewModelScope)
    }

    fun onLoadMoreProductError() {
        viewModelScope.launch {
            _snackbarEvent.send("Maaf, tidak dapat memuat produk lainnya saat ini. Silakan coba beberapa saat lagi.")
        }
    }
}