package store.andefi.ui.product.state

import store.andefi.utility.SortBy

data class ProductReviewAndRatingUiState(
    val showRatingBottomSheet: Boolean = false,
    val showSortBottomSheet: Boolean = false,
    val sortFilter: String = SortBy.DEFAULT_SORT_BY,
    val ratingFilter: String? = null
)