package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemCheckoutRequestDto(
    @SerialName("product_id")
    val productId: String,
    val quantity: Long
)
