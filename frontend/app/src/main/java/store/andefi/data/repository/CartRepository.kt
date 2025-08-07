package store.andefi.data.repository

import store.andefi.data.remote.api.CartApi
import store.andefi.data.remote.dto.CartItemUpdateQuantityRequestDto
import store.andefi.data.remote.dto.CartResponseDto
import store.andefi.exception.AuthenticationFailedException
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartApi: CartApi
) {
    suspend fun addProductIntoCart(token: String, productId: String): Result<Unit> {
        try {
            val response =
                cartApi.addProductIntoCart("Bearer $token", mapOf("product_id" to productId))

            return when (response.code()) {
                200 -> Result.success(Unit)
                401 -> Result.failure(AuthenticationFailedException())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getCart(token: String): Result<CartResponseDto> {
        try {
            val response =
                cartApi.getOrCreateCart("Bearer $token")

            return when (response.code()) {
                200 -> Result.success(requireNotNull(response.body()))
                401 -> Result.failure(AuthenticationFailedException())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun removeProductFromCart(token: String, productId: String): Result<Unit> {
        try {
            val response =
                cartApi.removeProductFromCart("Bearer $token", mapOf("product_id" to productId))

            return when (response.code()) {
                200 -> Result.success(Unit)
                401 -> Result.failure(AuthenticationFailedException())
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateCartItemQuantityInCart(
        token: String,
        cartItemUpdateQuantityRequestDto: CartItemUpdateQuantityRequestDto
    ): Result<Unit> {
        try {
            val response =
                cartApi.updateCartItemQuantityInCart(
                    "Bearer $token",
                    cartItemUpdateQuantityRequestDto
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