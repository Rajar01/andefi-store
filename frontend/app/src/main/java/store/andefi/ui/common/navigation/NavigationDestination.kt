package store.andefi.ui.common.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.ProductResponseDto

@Serializable
object AuthGraph

@Serializable
object SignInFormRoute

@Serializable
object SignUpFormRoute

@Serializable
data class SignUpEmailConfirmationRoute(val email: String)

@Serializable
object SignUpSuccessRoute

@Serializable
object ForgotPasswordFormRoute

@Serializable
data class ForgotPasswordEmailConfirmationRoute(val email: String)

@Serializable
data class ResetPasswordFormRoute(val token: String)

@Serializable
object ResetPasswordSuccessRoute

@Serializable
object MainGraph

@Serializable
object ProductCatalogRoute

@Serializable
object ProductCategoriesRoute

@Serializable
data class ProductCatalogFilteredByCategoryRoute(val category: String)

@Serializable
data class ProductDetailsRoute(val productId: String)

@Serializable
data class ProductSpecificationsAndDescriptionRoute(
    val product: ProductResponseDto?
)

@Serializable
data class ProductReviewAndRatingRoute(val productId: String)

@Serializable
object CartRoute

@Serializable
object OrderHistoryRoute

@Serializable
data class CheckoutRoute(val orderCheckoutRequestDto: OrderCheckoutRequestDto)

@Serializable
object ShippingAddressRoute

@Serializable
data class PaymentSuccessRoute(@SerialName("order_id") val orderId: String)

@Serializable
object PaymentFailedRoute

@Serializable
data class OrderDetailRoute(@SerialName("order_id") val orderId: String)