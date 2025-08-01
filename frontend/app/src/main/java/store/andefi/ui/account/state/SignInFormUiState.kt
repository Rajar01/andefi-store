package store.andefi.ui.account.state

data class SignInFormUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val errorMessage: String? = null,
)
