package store.andefi.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.repository.ProductRepository
import store.andefi.ui.product.state.ProductCategoriesUiState
import javax.inject.Inject

@HiltViewModel
class ProductCategoriesViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductCategoriesUiState())
    val uiState: StateFlow<ProductCategoriesUiState> = _uiState.asStateFlow()
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isLoading = true, isError = false)

            productRepository.getCategories()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        categories = it,
                        isLoading = false,
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
    }
}