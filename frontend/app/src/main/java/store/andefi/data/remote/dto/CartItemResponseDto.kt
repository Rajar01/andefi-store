package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponseDto(
    val id: String,
    val product: ProductResponseDto,
    val quantity: Long,
)
