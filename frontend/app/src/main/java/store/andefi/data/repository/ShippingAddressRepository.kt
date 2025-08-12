package store.andefi.data.repository

import store.andefi.data.remote.api.ShippingAddressApi
import store.andefi.data.remote.dto.ShippingAddressResponseDto
import store.andefi.data.remote.dto.ShippingAddressUpdateRequestDto
import store.andefi.exception.AuthenticationFailedException
import javax.inject.Inject

class ShippingAddressRepository @Inject constructor(
    private val shippingAddressApi: ShippingAddressApi
) {
    suspend fun getAccountShippingAddress(token: String): Result<ShippingAddressResponseDto> {
        try {
            val response =
                shippingAddressApi.getAccountShippingAddress("Bearer $token")

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                401 -> Result.failure(AuthenticationFailedException())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateAccountShippingAddress(
        token: String,
        shippingAddressUpdateRequestDto: ShippingAddressUpdateRequestDto
    ): Result<Unit> {
        try {
            val response =
                shippingAddressApi.updateAccountShippingAddress(
                    "Bearer $token",
                    shippingAddressUpdateRequestDto
                )

            return when (response.code()) {
                200 -> Result.success(Unit)
                401 -> Result.failure(AuthenticationFailedException())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}