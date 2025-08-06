package store.andefi.data.repository

import store.andefi.data.remote.api.CartApi
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
}