package store.andefi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import store.andefi.data.pagingsource.ProductReviewsPagingSource
import store.andefi.data.pagingsource.ProductsPagingSource
import store.andefi.data.pagingsource.ReviewMediaPagingSource
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.remote.dto.CategoryResponseDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.remote.dto.ReviewResponseDto
import store.andefi.data.remote.dto.ReviewsSummaryResponseDto
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi
) {
    suspend fun getProductById(productId: String): Result<ProductResponseDto> {
        try {
            val response = productApi.getProductById(productId)

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getProducts(
        keyword: String? = null,
        category: String? = null,
        limit: Int = 10,
    ): Flow<PagingData<ProductResponseDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = {
                ProductsPagingSource(productApi, category = category, keyword = keyword)
            }
        ).flow
    }

    suspend fun getCategories(): Result<List<CategoryResponseDto>> {
        try {
            val response = productApi.getCategories()

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getProductReview(productId: String): Result<ReviewResponseDto> {
        try {
            val response = productApi.getProductReviews(
                productId,
                rating = null,
                limit = 1,
                cursor = null
            )

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()).reviews.first())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getProductReviewSummary(productId: String): Result<ReviewsSummaryResponseDto> {
        try {
            val response = productApi.getProductReviewSummary(productId)

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getProductReviewMedia(
        productId: String,
        limit: Int = 10
    ): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = {
                ReviewMediaPagingSource(productApi, productId = productId)
            }
        ).flow
    }

    fun getProductReviews(
        productId: String,
        rating: String? = null,
        sortBy: String? = null,
        limit: Int = 10,
    ): Flow<PagingData<ReviewResponseDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = {
                ProductReviewsPagingSource(
                    productApi,
                    productId = productId,
                    rating = rating,
                    sortBy = sortBy,
                )
            }
        ).flow
    }
}