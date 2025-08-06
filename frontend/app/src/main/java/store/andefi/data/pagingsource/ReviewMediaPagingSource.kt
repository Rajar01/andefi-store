package store.andefi.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import store.andefi.data.remote.api.ProductApi

class ReviewMediaPagingSource(
    val productApi: ProductApi,
    val productId: String,
) : PagingSource<String, String>() {
    override fun getRefreshKey(state: PagingState<String, String>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, String> {
        return try {
            val cursor = params.key

            val response = productApi.getProductReviewMedia(
                cursor = cursor,
                productId = productId,
                limit = null,
            )

            val data = response.body()?.media?.mapNotNull {
                it.urls["image"]?.split(
                    "|"
                )
            }?.flatten()?.map { it.trim() }

            LoadResult.Page(
                data = data as List<String>,
                prevKey = null,
                nextKey = if (response.body()?.hasMore!!) response.body()?.nextCursor else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}