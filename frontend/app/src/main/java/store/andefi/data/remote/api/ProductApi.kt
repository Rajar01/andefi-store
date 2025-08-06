package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import store.andefi.data.remote.dto.CategoryResponseDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.remote.dto.ProductsResponseDto
import store.andefi.data.remote.dto.ReviewMediaResponseDto
import store.andefi.data.remote.dto.ReviewsResponseDto
import store.andefi.data.remote.dto.ReviewsSummaryResponseDto
import store.andefi.utility.SortBy

interface ProductApi {
    @GET("/api/products/{product_id}")
    suspend fun getProductById(@Path("product_id") productId: String): Response<ProductResponseDto>

    @GET("/api/products")
    suspend fun getProducts(
        @Query("category") category: String?,
        @Query("limit") limit: Int?,
        @Query("cursor") cursor: String?
    ): Response<ProductsResponseDto>

    @GET("/api/products/search")
    suspend fun search(
        @Query("keyword") keyword: String?,
        @Query("limit") limit: Int?,
        @Query("cursor") cursor: String?
    ): Response<ProductsResponseDto>

    @GET("/api/categories")
    suspend fun getCategories(): Response<List<CategoryResponseDto>>

    @GET("/api/reviews")
    suspend fun getProductReviews(
        @Query("product_id") productId: String,
        @Query("rating") rating: Int?,
        @Query("sort_by") sortBy: String = SortBy.DEFAULT_SORT_BY,
        @Query("limit") limit: Int?,
        @Query("cursor") cursor: String?
    ): Response<ReviewsResponseDto>

    @GET("/api/reviews/summary")
    suspend fun getProductReviewSummary(@Query("product_id") productId: String): Response<ReviewsSummaryResponseDto>

    @GET("/api/reviews/media")
    suspend fun getProductReviewMedia(
        @Query("product_id") productId: String,
        @Query("limit") limit: Int?,
        @Query("cursor") cursor: String?
    ): Response<ReviewMediaResponseDto>
}