package store.andefi.ui.product.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.ui.common.navigation.ProductSpecificationsAndDescriptionRoute
import store.andefi.ui.common.navigation.navtype.ProductNavType
import store.andefi.ui.product.state.ProductSpecificationsAndDescriptionUiState
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class ProductSpecificationsAndDescriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args =
        savedStateHandle.toRoute<ProductSpecificationsAndDescriptionRoute>(typeMap = mapOf(typeOf<ProductResponseDto?>() to ProductNavType))

    private val _uiState = MutableStateFlow(ProductSpecificationsAndDescriptionUiState())
    val uiState: StateFlow<ProductSpecificationsAndDescriptionUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(product = args.product)
    }
}