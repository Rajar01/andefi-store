package store.andefi.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.remote.dto.ReviewResponseDto
import store.andefi.utility.SortBy

class ProductReviewsPagingSource(
    val productApi: ProductApi,
    val productId: String,
    val rating: String? = null,
    val sortBy: String? = null,
) : PagingSource<String, ReviewResponseDto>() {
    override fun getRefreshKey(state: PagingState<String, ReviewResponseDto>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ReviewResponseDto> {
        return try {
            val cursor = params.key

            val response = productApi.getProductReviews(
                productId = productId,
                rating = rating?.toInt(),
                sortBy = sortBy ?: SortBy.DEFAULT_SORT_BY,
                limit = null,
                cursor = cursor
            )

            LoadResult.Page(
                data = response.body()?.reviews as List<ReviewResponseDto>,
                prevKey = null,
                nextKey = if (response.body()?.hasMore!!) response.body()?.nextCursor else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}