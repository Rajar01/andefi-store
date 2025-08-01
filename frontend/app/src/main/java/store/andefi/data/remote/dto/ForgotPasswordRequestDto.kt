package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequestDto(
    val email: String
)
