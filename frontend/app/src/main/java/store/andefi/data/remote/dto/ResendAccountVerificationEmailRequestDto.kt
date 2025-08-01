package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResendAccountVerificationEmailRequestDto(
    val email: String
)
