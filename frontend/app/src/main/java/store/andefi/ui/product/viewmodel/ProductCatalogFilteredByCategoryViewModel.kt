package store.andefi.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductCatalogFilteredByCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun getProducts(category: String): Flow<PagingData<ProductResponseDto>> {
        return productRepository.getProducts(category = category)
            .cachedIn(viewModelScope)
    }

    fun onLoadMoreProductError() {
        viewModelScope.launch {
            _snackbarEvent.send("Maaf, tidak dapat memuat produk lainnya saat ini. Silakan coba beberapa saat lagi.")
        }
    }
}