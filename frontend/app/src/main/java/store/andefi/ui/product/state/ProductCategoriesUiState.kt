package store.andefi.ui.product.state

import store.andefi.data.remote.dto.CategoryResponseDto

data class ProductCategoriesUiState(
    val categories: List<CategoryResponseDto> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
