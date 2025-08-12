package store.andefi.data.repository

import retrofit2.http.Query
import store.andefi.data.remote.api.OrderApi
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.OrderCheckoutResponseDto
import store.andefi.data.remote.dto.OrderResponseDto
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderApi: OrderApi
) {
    suspend fun checkoutOrder(
        token: String,
        orderCheckoutRequestDto: OrderCheckoutRequestDto
    ): Result<OrderCheckoutResponseDto> {
        try {
            val response = orderApi.checkoutOrder(
                token = "Bearer $token",
                orderCheckoutRequestDto = orderCheckoutRequestDto
            )

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getOrders(
        token: String,
        orderStatus: String? = null,
        period: String? = null
    ): Result<List<OrderResponseDto>> {
        try {
            val response = orderApi.getOrders(
                token = "Bearer $token",
                orderStatus = orderStatus,
                period = period
            )

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getOrder(token: String, orderId: String): Result<OrderResponseDto> {
        try {
            val response = orderApi.getOrder(
                token = "Bearer $token",
                orderId = orderId
            )

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}