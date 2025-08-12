package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.OrderCheckoutResponseDto
import store.andefi.data.remote.dto.OrderResponseDto

interface OrderApi {
    @POST("/api/orders/checkout")
    suspend fun checkoutOrder(
        @Header("Authorization") token: String,
        @Body orderCheckoutRequestDto: OrderCheckoutRequestDto
    ): Response<OrderCheckoutResponseDto>

    @GET("/api/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("order_status") orderStatus: String?,
        @Query("period") period: String?
    ): Response<List<OrderResponseDto>>


    @GET("/api/orders/{order_id}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("order_id") orderId: String
    ): Response<OrderResponseDto>
}