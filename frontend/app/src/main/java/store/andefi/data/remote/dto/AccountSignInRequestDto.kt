package store.andefi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountSignInRequestDto(
    val email: String, val password: String
)