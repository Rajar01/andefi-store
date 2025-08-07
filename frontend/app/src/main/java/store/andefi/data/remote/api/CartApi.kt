package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import store.andefi.data.remote.dto.CartItemUpdateQuantityRequestDto
import store.andefi.data.remote.dto.CartResponseDto

interface CartApi {
    @POST("/api/carts/items")
    @Headers("Content-Type: application/json")
    suspend fun addProductIntoCart(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Response<Unit>

    @GET("/api/carts")
    suspend fun getOrCreateCart(@Header("Authorization") token: String): Response<CartResponseDto>
    
    @HTTP(method = "DELETE", path = "/api/carts/items", hasBody = true)
    @Headers("Content-Type: application/json")
    suspend fun removeProductFromCart(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Response<Unit>

    @PUT("/api/carts/items")
    suspend fun updateCartItemQuantityInCart(
        @Header("Authorization") token: String,
        @Body cartItemUpdateQuantityRequestDto: CartItemUpdateQuantityRequestDto
    ): Response<Unit>
}