package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    val password: String
)
