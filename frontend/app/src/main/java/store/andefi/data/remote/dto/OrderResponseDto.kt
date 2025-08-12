package store.andefi.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.utility.serializer.InstantSerializer
import java.time.Instant

@Serializable
data class OrderResponseDto(
    val id: String,
    @SerialName("account_id") val accountId: String,
    @SerialName("order_items") val orderItems: MutableList<OrderItemResponseDto>,
    val shippingAddress: ShippingAddressResponseDto,
    val payment: PaymentResponseDto,
    val status: String,
    @Serializable(with = InstantSerializer::class) @SerialName("paid_at") @Contextual val paidAt: Instant?,
    @Serializable(with = InstantSerializer::class) @SerialName("shipping_at") @Contextual val shippingAt: Instant?,
    @Serializable(with = InstantSerializer::class) @SerialName("completed_at") @Contextual val completedAt: Instant?,
    @Serializable(with = InstantSerializer::class) @SerialName("created_at") @Contextual val createdAt: Instant,
)
