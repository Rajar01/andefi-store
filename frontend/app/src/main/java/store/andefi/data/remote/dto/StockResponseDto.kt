package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockResponseDto(
    val id: String,
    @SerialName("product_id") val productId: String,
    @SerialName("quantity_on_hand") val quantityOnHand: Long,
    @SerialName("reserved_quantity") val reservedQuantity: Long,
    @SerialName("available_quantity") val availableQuantity: Long,
    @SerialName("sold_quantity") val soldQuantity: Long,
)
