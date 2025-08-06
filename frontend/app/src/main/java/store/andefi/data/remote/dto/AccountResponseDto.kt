package store.andefi.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.utility.InstantSerializer
import java.time.Instant

@Serializable
data class AccountResponseDto(
    val id: String,
    val email: String,
    @SerialName("full_name") val fullName: String,
    val username: String,
    @SerialName("phone_number") val phoneNumber: String,
    val shippingAddress: ShippingAddressResponseDto,
    val roles: MutableSet<String?>,
    @SerialName("is_verified") val isVerified: Boolean,
    @Serializable(with = InstantSerializer::class) @SerialName("verified_at") @Contextual val verifiedAt: Instant?,
)
