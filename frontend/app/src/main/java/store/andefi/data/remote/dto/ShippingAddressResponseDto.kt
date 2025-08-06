package store.andefi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShippingAddressResponseDto(
    val id: String,
    val address: String,
    @SerialName("address_other_details") val addressOtherDetails: String?
)
