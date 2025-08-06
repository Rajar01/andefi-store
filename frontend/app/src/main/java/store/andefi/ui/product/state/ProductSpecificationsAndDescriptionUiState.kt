package store.andefi.ui.product.state

import store.andefi.data.remote.dto.ProductResponseDto

data class ProductSpecificationsAndDescriptionUiState(
    val product: ProductResponseDto? = null,
)
