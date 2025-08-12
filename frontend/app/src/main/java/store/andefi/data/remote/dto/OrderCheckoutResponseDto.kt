package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderCheckoutResponseDto(
    @SerialName("order_id") val orderId: String,
    @SerialName("transaction_token") val paymentRedirectUrl: String,
    @SerialName("total_amount_before_discount") val totalAmountBeforeDiscount: Long,
    @SerialName("total_amount_after_discount") val totalAmountAfterDiscount: Long,
    @SerialName("total_discount") val totalDiscount: Long,
)
