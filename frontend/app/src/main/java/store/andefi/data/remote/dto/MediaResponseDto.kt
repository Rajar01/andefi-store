package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MediaResponseDto(
    val id: String,
    val urls: MutableMap<String, String>
)
