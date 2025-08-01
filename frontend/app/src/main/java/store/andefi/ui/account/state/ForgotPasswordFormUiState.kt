package store.andefi.ui.account.state

data class ForgotPasswordFormUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSendPasswordResetEmailSuccessful: Boolean = false,
    val errorMessage: String? = null,
)
