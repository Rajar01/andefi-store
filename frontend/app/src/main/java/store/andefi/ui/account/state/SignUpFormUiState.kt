package store.andefi.ui.account.state

data class SignUpFormUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val errorMessage: String? = null,
)
