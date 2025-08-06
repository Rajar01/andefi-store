package store.andefi.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.remote.dto.ProductResponseDto

class ProductsPagingSource(
    val productApi: ProductApi,
    val category: String? = null,
    val keyword: String? = null,
) : PagingSource<String, ProductResponseDto>() {
    override fun getRefreshKey(state: PagingState<String, ProductResponseDto>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ProductResponseDto> {
        return try {
            val cursor = params.key

            val response = if (keyword != null) productApi.search(
                cursor = cursor,
                keyword = keyword,
                limit = null,
            ) else productApi.getProducts(
                cursor = cursor,
                category = category,
                limit = null,
            )

            LoadResult.Page(
                data = response.body()?.products as List<ProductResponseDto>,
                prevKey = null,
                nextKey = if (response.body()?.hasMore!!) response.body()?.nextCursor else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}