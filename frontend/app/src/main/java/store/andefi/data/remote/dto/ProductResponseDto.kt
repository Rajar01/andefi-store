package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class ProductResponseDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Long,
    val attributes: MutableMap<String, String>,
    val categories: MutableList<CategoryResponseDto>,
    val media: MediaResponseDto,
    val discount: DiscountResponseDto,
    val stock: StockResponseDto,
)
