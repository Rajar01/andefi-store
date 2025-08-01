package store.andefi.data.remote.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AccountSignUpRequestDto @OptIn(ExperimentalSerializationApi::class) constructor(
    val email: String,
    val username: String,
    val password: String,
    @EncodeDefault val roles: MutableSet<String> = mutableSetOf("customer"),

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("full_name")
    val fullName: String,
)
