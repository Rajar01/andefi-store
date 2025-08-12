package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import store.andefi.data.remote.dto.ShippingAddressResponseDto
import store.andefi.data.remote.dto.ShippingAddressUpdateRequestDto

interface ShippingAddressApi {
    @GET("/api/shipping-addresses")
    suspend fun getAccountShippingAddress(
        @Header("Authorization") token: String
    ): Response<ShippingAddressResponseDto>

    @PUT("/api/shipping-addresses")
    suspend fun updateAccountShippingAddress(
        @Header("Authorization") token: String,
        @Body shippingAddressUpdateRequestDto: ShippingAddressUpdateRequestDto
    ): Response<Unit>
}