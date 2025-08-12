package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemResponseDto(
    val id: String,
    @SerialName("product_id") val productId: String,
    @SerialName("product_name") val productName: String,
    @SerialName("product_price") val productPrice: Long,
    @SerialName("product_discount_percentage") val productDiscountPercentage: Long,
    @SerialName("product_media_urls") val productMediaUrls: MutableMap<String, String>,
    val quantity: Long
)
