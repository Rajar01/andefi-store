package store.andefi.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.utility.serializer.InstantSerializer
import java.time.Instant


@Serializable
data class ReviewResponseDto(
    val id: String,
    val content: String,
    val rating: Float,
    val account: AccountResponseDto,
    @SerialName("product_id") val productId: String,
    val media: MediaResponseDto?,
    @Serializable(with = InstantSerializer::class) @SerialName("created_at") @Contextual val createdAt: Instant,
)
