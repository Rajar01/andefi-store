package store.andefi.ui.product.state

import store.andefi.data.remote.dto.MediaResponseDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.remote.dto.ReviewResponseDto
import store.andefi.data.remote.dto.ReviewsSummaryResponseDto

data class ProductDetailsUiState(
    val product: ProductResponseDto? = null,
    val review: ReviewResponseDto? = null,
    val reviewSummary: ReviewsSummaryResponseDto? = null,
    val reviewMedia: List<MediaResponseDto> = emptyList(),
    val isAddProductIntoCartSuccessful: Boolean = false,
    val isAddProductIntoCartLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
