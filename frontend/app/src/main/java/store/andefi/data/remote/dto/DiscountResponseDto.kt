package store.andefi.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.utility.InstantSerializer
import java.time.Instant

@Serializable
data class DiscountResponseDto(
    val id: String,
    @SerialName("discount_percentage") val discountPercentage: Long,
    @SerialName("is_active") val isActive: Boolean,
    @Serializable(with = InstantSerializer::class) @SerialName("start_date") @Contextual val startDate: Instant,
    @Serializable(with = InstantSerializer::class) @SerialName("end_date") @Contextual val endDate: Instant,
)
