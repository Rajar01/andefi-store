package store.andefi.ui.common.navigation

import kotlinx.serialization.Serializable

@Serializable
object AuthGraph

@Serializable
object SignInForm

@Serializable
object SignUpForm

@Serializable
data class SignUpEmailConfirmation(val email: String)

@Serializable
object SignUpSuccess

@Serializable
object ForgotPasswordForm

@Serializable
data class ForgotPasswordEmailConfirmation(val email: String)

@Serializable
data class ResetPasswordForm(val token: String)

@Serializable
object ResetPasswordSuccess