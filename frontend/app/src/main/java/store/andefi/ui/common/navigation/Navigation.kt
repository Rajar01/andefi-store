package store.andefi.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import store.andefi.ui.account.screen.ForgotPasswordEmailConfirmationScreen
import store.andefi.ui.account.screen.ForgotPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordFormScreen
import store.andefi.ui.account.screen.ResetPasswordSuccessScreen
import store.andefi.ui.account.screen.SignInScreen
import store.andefi.ui.account.screen.SignUpEmailConfirmationScreen
import store.andefi.ui.account.screen.SignUpFormScreen
import store.andefi.ui.account.screen.SignUpSuccessScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AuthGraph) {
        navigation<AuthGraph>(startDestination = SignInForm) {
            composable<SignInForm> {
                SignInScreen(
                    navigateToSignUpForm = { navController.navigate(SignUpForm) },
                    navigateToForgotPassword = { navController.navigate(ForgotPasswordForm) }
                )
            }
            composable<SignUpForm> {
                SignUpFormScreen(
                    navigateToSignUpEmailConfirmation = {
                        navController.navigate(
                            SignUpEmailConfirmation(it)
                        )
                    },
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInForm
                        )
                    }
                )
            }
            composable<SignUpEmailConfirmation> {
                val args = it.toRoute<SignUpEmailConfirmation>()

                SignUpEmailConfirmationScreen(email = args.email)
            }
            composable<SignUpSuccess>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = "https://andefi.store/accounts/verified" }
                )
            ) {
                SignUpSuccessScreen(
                    navigateToSignInForm = {
                        navController.navigate(SignInForm)
                    }
                )
            }
            composable<ForgotPasswordForm> {
                ForgotPasswordFormScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInForm
                        )
                    },
                    navigateToForgotPasswordEmailConfirmation = {
                        navController.navigate(
                            ForgotPasswordEmailConfirmation(it)
                        )
                    },
                )
            }
            composable<ForgotPasswordEmailConfirmation> {
                val args = it.toRoute<ForgotPasswordEmailConfirmation>()

                ForgotPasswordEmailConfirmationScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInForm
                        )
                    },
                    email = args.email
                )
            }
            composable<ResetPasswordForm>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://andefi.store/accounts/reset-password?token={token}"
                    }
                )
            ) {
                val args = it.toRoute<ResetPasswordForm>()

                ResetPasswordFormScreen(
                    navigateToResetPasswordSuccess = {
                        navController.navigate(
                            ResetPasswordSuccess
                        )
                    },
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInForm
                        )
                    },
                    token = args.token
                )
            }
            composable<ResetPasswordSuccess> {
                ResetPasswordSuccessScreen(
                    navigateToSignInForm = {
                        navController.navigate(
                            SignInForm
                        )
                    },
                )
            }
        }
    }
}