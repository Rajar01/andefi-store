package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class CategoryResponseDto(
    val id: String,
    val name: String
)
