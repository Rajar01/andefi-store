package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CartResponseDto(
    val id: String,
    val account: AccountResponseDto,

    @SerialName("cart_items")
    val cartItems: MutableList<CartItemResponseDto>
)
