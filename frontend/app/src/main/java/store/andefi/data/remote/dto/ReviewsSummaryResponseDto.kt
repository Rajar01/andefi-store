package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsSummaryResponseDto(
    @SerialName("product_id") val productId: String,
    @SerialName("total_reviews") val totalReviews: Long,
    @SerialName("total_ratings") val totalRatings: Long,
    @SerialName("average_ratings") val averageRatings: Float
)
