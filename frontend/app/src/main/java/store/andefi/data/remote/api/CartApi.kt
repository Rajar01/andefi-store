package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CartApi {
    @POST("/api/carts/items")
    @Headers("Content-Type: application/json")
    suspend fun addProductIntoCart(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Response<Unit>
}