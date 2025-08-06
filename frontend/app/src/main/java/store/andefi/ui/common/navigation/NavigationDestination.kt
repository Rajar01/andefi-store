package store.andefi.ui.common.navigation

import kotlinx.serialization.Serializable
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
object ProductGraph

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