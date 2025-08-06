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
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.ui.account.screen.ForgotPasswordEmailConfirmationScreen
import store.andefi.ui.account.screen.ForgotPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordSuccessScreen
import store.andefi.ui.account.screen.SignInScreen
import store.andefi.ui.account.screen.SignUpEmailConfirmationScreen
import store.andefi.ui.account.screen.SignUpFormScreen
import store.andefi.ui.account.screen.SignUpSuccessScreen
import store.andefi.ui.common.navigation.navtype.ProductNavType
import store.andefi.ui.product.component.BottomNavigationBar
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
                SignInScreen(
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
                    navDeepLink { uriPattern = "https://andefi.store/accounts/verified" })
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
                    }, navigateToSignInForm = {
                        navController.navigate(
                            SignInFormRoute
                        )
                    }, token = args.token
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

        navigation<ProductGraph>(startDestination = ProductCatalogRoute) {
            composable<ProductCatalogRoute> {
                ProductCatalogScreen(
                    navigateToProductDetailsRoute = { navController.navigate(ProductDetailsRoute(it)) },
                    bottomNavigationBar = {
                        BottomNavigationBar(
                            navigateToProductCatalogRoute = {
                                navController.navigate(ProductCatalogRoute)
                            },
                            navigateToProductCategoriesRoute = {
                                navController.navigate(ProductCategoriesRoute)
                            },
                            isProductCatalogRouteSelected = currentDestination?.route?.contains("ProductCatalogRoute") == true,
                            isProductCategoriesRouteSelected = currentDestination?.route?.contains("ProductCategoriesRoute") == true,
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
                            isProductCatalogRouteSelected = currentDestination?.route?.contains("ProductCatalogRoute") == true,
                            isProductCategoriesRouteSelected = currentDestination?.route?.contains("ProductCategoriesRoute") == true,
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
                ProductDetailsScreen(navigateBack = {
                    navController.popBackStack()
                }, navigateToProductSpecificationsAndDescriptionRoute = {
                    navController.navigate(
                        ProductSpecificationsAndDescriptionRoute(it)
                    )
                }, navigateToProductReviewAndRatingRoute = {
                    navController.navigate(ProductReviewAndRatingRoute(it))
                })
            }
            composable<ProductSpecificationsAndDescriptionRoute>(typeMap = mapOf(typeOf<ProductResponseDto?>() to ProductNavType)) {
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
                    }, productId = args.productId
                )
            }
        }
    }
}