package store.andefi.ui.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.ui.account.screen.ForgotPasswordEmailConfirmationScreen
import store.andefi.ui.account.screen.ForgotPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordSuccessScreen
import store.andefi.ui.account.screen.SignInScreen
import store.andefi.ui.account.screen.SignUpEmailConfirmationScreen
import store.andefi.ui.account.screen.SignUpFormScreen
import store.andefi.ui.account.screen.SignUpSuccessScreen
import store.andefi.ui.cart.screen.CartScreen
import store.andefi.ui.common.navigation.navtype.OrderCheckoutRequestDtoNavType
import store.andefi.ui.common.navigation.navtype.ProductResponseDtoNavType
import store.andefi.ui.common.viewmodel.SharedViewModel
import store.andefi.ui.common.viewmodel.sharedViewModel
import store.andefi.ui.order.screen.CheckoutScreen
import store.andefi.ui.order.screen.OrderDetailScreen
import store.andefi.ui.order.screen.OrderHistoryScreen
import store.andefi.ui.order.screen.PaymentFailedScreen
import store.andefi.ui.order.screen.PaymentSuccessScreen
import store.andefi.ui.order.screen.ShippingAddressScreen
import store.andefi.ui.product.component.BottomNavigationBar
import store.andefi.ui.product.screen.ProductARScreen
import store.andefi.ui.product.screen.ProductCatalogFilteredByCategoryScreen
import store.andefi.ui.product.screen.ProductCatalogScreen
import store.andefi.ui.product.screen.ProductCategoriesScreen
import store.andefi.ui.product.screen.ProductDetailsScreen
import store.andefi.ui.product.screen.ProductReviewAndRatingScreen
import store.andefi.ui.product.screen.ProductSpecificationsAndDescriptionScreen
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@Composable
fun Navigation(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavHost(navController = navController, startDestination = AuthGraph) {
        navigation<AuthGraph>(startDestination = SignInFormRoute) {
            composable<SignInFormRoute> {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController
                )

                SignInScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToSignUpForm = { navController.navigate(SignUpFormRoute) },
                    navigateToForgotPassword = { navController.navigate(ForgotPasswordFormRoute) },
                    navigateToProductCatalogRoute = {
                        navController.navigate(ProductCatalogRoute)
                    }
                )
            }
            composable<SignUpFormRoute> {
                SignUpFormScreen(navigateToSignUpEmailConfirmation = {
                    navController.navigate(
                        SignUpEmailConfirmationRoute(it)
                    )
                }, navigateToSignInForm = {
                    navController.navigate(
                        SignInFormRoute
                    )
                })
            }
            composable<SignUpEmailConfirmationRoute> {
                val args = it.toRoute<SignUpEmailConfirmationRoute>()

                SignUpEmailConfirmationScreen(email = args.email)
            }
            composable<SignUpSuccessRoute>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = "https://andefi.store/accounts/verified" }
                )
            ) {
                SignUpSuccessScreen(
                    navigateToSignInForm = {
                        navController.navigate(SignInFormRoute)
                    })
            }
            composable<ForgotPasswordFormRoute> {
                ForgotPasswordFormScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInFormRoute
                        )
                    },
                    navigateToForgotPasswordEmailConfirmation = {
                        navController.navigate(
                            ForgotPasswordEmailConfirmationRoute(it)
                        )
                    },
                )
            }
            composable<ForgotPasswordEmailConfirmationRoute> {
                val args = it.toRoute<ForgotPasswordEmailConfirmationRoute>()

                ForgotPasswordEmailConfirmationScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInFormRoute
                        )
                    }, email = args.email
                )
            }
            composable<ResetPasswordFormRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://andefi.store/accounts/reset-password?token={token}"
                    })
            ) {
                val args = it.toRoute<ResetPasswordFormRoute>()

                ResetPasswordFormScreen(
                    navigateToResetPasswordSuccess = {
                        navController.navigate(
                            ResetPasswordSuccessRoute
                        )
                    },
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInFormRoute
                        )
                    },
                    token = args.token
                )
            }
            composable<ResetPasswordSuccessRoute> {
                ResetPasswordSuccessScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInFormRoute
                        )
                    },
                )
            }
        }

        navigation<MainGraph>(startDestination = ProductCatalogRoute) {
            composable<ProductCatalogRoute> {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController
                )

                ProductCatalogScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToProductDetailsRoute = { navController.navigate(ProductDetailsRoute(it)) },
                    bottomNavigationBar = {
                        BottomNavigationBar(
                            navigateToProductCatalogRoute = {
                                navController.navigate(ProductCatalogRoute)
                            },
                            navigateToProductCategoriesRoute = {
                                navController.navigate(ProductCategoriesRoute)
                            },
                            navigateToCartRoute = {
                                navController.navigate(CartRoute)
                            },
                            navigateToOrderHistoryRoute = {
                                navController.navigate(OrderHistoryRoute)
                            },
                            isProductCatalogRouteSelected = currentDestination?.route?.contains(
                                ProductCatalogRoute.javaClass.typeName
                            ) == true,
                            isProductCategoriesRouteSelected = currentDestination?.route?.contains(
                                ProductCategoriesRoute.javaClass.typeName
                            ) == true,
                            isCartRouteSelected = currentDestination?.route?.contains(CartRoute.javaClass.typeName) == true,
                            isOrderHistoryRouteSelected = currentDestination?.route?.contains(
                                OrderHistoryRoute.javaClass.typeName
                            ) == true,
                        )
                    },
                )
            }
            composable<ProductCategoriesRoute> {
                ProductCategoriesScreen(
                    navigateToProductCatalogFilteredByCategoryRoute = {
                        navController.navigate(
                            ProductCatalogFilteredByCategoryRoute(it)
                        )
                    },
                    bottomNavigationBar = {
                        BottomNavigationBar(
                            navigateToProductCatalogRoute = {
                                navController.navigate(ProductCatalogRoute)
                            },
                            navigateToProductCategoriesRoute = {
                                navController.navigate(ProductCategoriesRoute)
                            },
                            navigateToCartRoute = {
                                navController.navigate(CartRoute)
                            },
                            navigateToOrderHistoryRoute = {
                                navController.navigate(OrderHistoryRoute)
                            },
                            isProductCatalogRouteSelected = currentDestination?.route?.contains(
                                ProductCatalogRoute.javaClass.typeName
                            ) == true,
                            isProductCategoriesRouteSelected = currentDestination?.route?.contains(
                                ProductCategoriesRoute.javaClass.typeName
                            ) == true,
                            isCartRouteSelected = currentDestination?.route?.contains(CartRoute.javaClass.typeName) == true,
                            isOrderHistoryRouteSelected = currentDestination?.route?.contains(
                                OrderHistoryRoute.javaClass.typeName
                            ) == true,
                        )
                    },
                )
            }
            composable<ProductCatalogFilteredByCategoryRoute> {
                val args = it.toRoute<ProductCatalogFilteredByCategoryRoute>()

                ProductCatalogFilteredByCategoryScreen(
                    navigateToProductCategoriesRoute = {
                        navController.navigate(ProductCategoriesRoute)
                    },
                    navigateToProductDetailsRoute = { navController.navigate(ProductDetailsRoute(it)) },
                    category = args.category
                )
            }
            composable<ProductDetailsRoute> {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController
                )

                ProductDetailsScreen(
                    sharedViewModel = sharedViewModel,
                    navigateBack = {
                        navController.popBackStack()
                    },
                    navigateToProductSpecificationsAndDescriptionRoute = {
                        navController.navigate(
                            ProductSpecificationsAndDescriptionRoute(it)
                        )
                    },
                    navigateToProductReviewAndRatingRoute = {
                        navController.navigate(ProductReviewAndRatingRoute(it))
                    },
                    navigateToProductArRoute = {
                        navController.navigate(ProductArRoute)
                    }
                )
            }
            composable<ProductSpecificationsAndDescriptionRoute>(typeMap = mapOf(typeOf<ProductResponseDto?>() to ProductResponseDtoNavType)) {
                ProductSpecificationsAndDescriptionScreen(
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
            composable<ProductReviewAndRatingRoute> {
                val args = it.toRoute<ProductReviewAndRatingRoute>()

                ProductReviewAndRatingScreen(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    productId = args.productId
                )
            }
            composable<CartRoute> {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController,
                )

                CartScreen(
                    sharedViewModel = sharedViewModel,
                    navigateBack = {
                        navController.popBackStack()
                    },
                    navigateToCheckoutRoute = { orderCheckoutRequestDto ->
                        navController.navigate(CheckoutRoute(orderCheckoutRequestDto))
                    }
                )
            }
            composable<CheckoutRoute>(typeMap = mapOf(typeOf<OrderCheckoutRequestDto>() to OrderCheckoutRequestDtoNavType)) {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController,
                )

                CheckoutScreen(
                    sharedViewModel = sharedViewModel,
                    navigateBack = {
                        navController.popBackStack()
                    },
                    navigateToShippingAddressRoute = {
                        navController.navigate(ShippingAddressRoute)
                    },
                    navigateToOrderDetailRoute = { orderId ->
                        navController.navigate(OrderDetailRoute(orderId))
                    }
                )
            }
            composable<ShippingAddressRoute> {
                ShippingAddressScreen()
            }
            composable<PaymentSuccessRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}"
                    },
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}&status_code=200&transaction_status=settlement"
                    }
                )
            ) {
                val args = it.toRoute<PaymentSuccessRoute>()

                PaymentSuccessScreen(navigateToOrderDetailRoute = {
                    navController.navigate(OrderDetailRoute(args.orderId))
                })
            }
            composable<PaymentFailedRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}&status_code=202&transaction_status=failure"
                    },
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}&status_code=202&transaction_status=deny"
                    },
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}&status_code=202&transaction_status=expire"
                    },
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/error"
                    }
                )
            ) {
                PaymentFailedScreen(
                    navigateToOrderHistoryRoute = {
                        navController.navigate(OrderHistoryRoute)
                    }
                )
            }
            composable<OrderDetailRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "https://andefi.store/payment/finish?order_id={order_id}&status_code=201&transaction_status=pending&action=back"
                    }
                )
            ) {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController,
                )

                OrderDetailScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToOrderHistoryRoute = {
                        navController.navigate(OrderHistoryRoute)
                    },
                )
            }
            composable<OrderHistoryRoute> {
                val sharedViewModel = it.sharedViewModel<SharedViewModel>(
                    navController = navController,
                )

                OrderHistoryScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToProductCatalogRoute = {
                        navController.navigate(ProductCatalogRoute)
                    },
                    navigateToOrderDetailRoute = { orderId ->
                        navController.navigate(OrderDetailRoute(orderId))
                    },
                    bottomNavigationBar = {
                        BottomNavigationBar(
                            navigateToProductCatalogRoute = {
                                navController.navigate(ProductCatalogRoute)
                            },
                            navigateToProductCategoriesRoute = {
                                navController.navigate(ProductCategoriesRoute)
                            },
                            navigateToCartRoute = {
                                navController.navigate(CartRoute)
                            },
                            navigateToOrderHistoryRoute = {
                                navController.navigate(OrderHistoryRoute)
                            },
                            isProductCatalogRouteSelected = currentDestination?.route?.contains(
                                ProductCatalogRoute.javaClass.typeName
                            ) == true,
                            isProductCategoriesRouteSelected = currentDestination?.route?.contains(
                                ProductCategoriesRoute.javaClass.typeName
                            ) == true,
                            isCartRouteSelected = currentDestination?.route?.contains(CartRoute.javaClass.typeName) == true,
                            isOrderHistoryRouteSelected = currentDestination?.route?.contains(
                                OrderHistoryRoute.javaClass.typeName
                            ) == true,
                        )
                    }
                )
            }
            composable<ProductArRoute> {
                ProductARScreen(
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}