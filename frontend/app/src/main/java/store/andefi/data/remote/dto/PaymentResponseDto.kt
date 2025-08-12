package store.andefi.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.utility.serializer.CurrencySerializer
import java.util.Currency

@Serializable
data class PaymentResponseDto(
    val id: String,
    @SerialName("account_id") val accountId: String,
    @SerialName("order_id") val orderId: String,
    val method: String?,
    @Serializable(with = CurrencySerializer::class) @Contextual val currency: Currency?,
    val amount: Long,
    @SerialName("transaction_id") val transactionId: String?,
    @SerialName("transaction_time") val transactionTime: String?,
    @SerialName("transaction_status") val transactionStatus: String?,
)
