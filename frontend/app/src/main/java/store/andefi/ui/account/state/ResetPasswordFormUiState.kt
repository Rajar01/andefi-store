package store.andefi.ui.account.state

data class ResetPasswordFormUiState(
    val password: String = "",
    val passwordConfirmation: String = "",
    val isLoading: Boolean = false,
    val isResetPasswordSuccessful: Boolean = false,
    val errorMessage: String? = null,
)
