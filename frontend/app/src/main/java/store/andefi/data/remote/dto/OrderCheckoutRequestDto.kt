package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrderCheckoutRequestDto(
    @SerialName("order_items")
    val orderItem: MutableList<OrderItemCheckoutRequestDto>
)
