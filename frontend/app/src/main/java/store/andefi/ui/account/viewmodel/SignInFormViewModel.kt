package store.andefi.ui.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.AccountSignInRequestDto
import store.andefi.exception.AuthenticationFailedException
import store.andefi.data.repository.AccountRepository
import store.andefi.ui.account.state.SignInFormUiState
import javax.inject.Inject

@HiltViewModel
class SignInFormViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInFormUiState())
    val uiState: StateFlow<SignInFormUiState> = _uiState.asStateFlow()


    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()


    fun onEmailValueChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordValueChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun signIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val email = _uiState.value.email
            val password = _uiState.value.password
            val accountSignInRequestDto = AccountSignInRequestDto(email, password)

            accountRepository.signIn(accountSignInRequestDto)
                .onSuccess { _ ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignInSuccessful = true,
                        errorMessage = null,
                    )
                    // TODO: Navigate into home page`
                }
                .onFailure { exception ->
                    when (exception) {
                        is AuthenticationFailedException -> _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSignInSuccessful = false,
                            errorMessage = "Email atau kata sandi yang Anda masukkan tidak valid. Silakan coba lagi"
                        )

                        else -> _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSignInSuccessful = false,
                            errorMessage = "Terjadi masalah. Silakan coba lagi nanti"
                        )
                    }

                    _snackbarEvent.send(_uiState.value.errorMessage!!)
                }
        }
    }
}