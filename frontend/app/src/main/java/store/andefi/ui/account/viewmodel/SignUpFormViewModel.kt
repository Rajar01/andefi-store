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
import store.andefi.data.remote.dto.AccountSignUpRequestDto
import store.andefi.data.repository.AccountRepository
import store.andefi.ui.account.state.SignUpFormUiState
import javax.inject.Inject

@HiltViewModel
class SignUpFormViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpFormUiState())
    val uiState: StateFlow<SignUpFormUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun onFullNameValueChanged(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName)
    }

    fun onUsernameValueChanged(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onEmailValueChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPhoneNumberValueChanged(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
    }

    fun onPasswordValueChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun signUp() {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // TODO: Validate sign up form input
            val fullName = _uiState.value.fullName
            val email = _uiState.value.email
            val username = _uiState.value.username
            val phoneNumber = _uiState.value.phoneNumber
            val password = _uiState.value.password
            val accountSignUpRequestDto = AccountSignUpRequestDto(
                fullName = fullName,
                email = email,
                username = username,
                phoneNumber = phoneNumber,
                password = password
            )

            accountRepository.signUp(accountSignUpRequestDto)
                .onSuccess { _ ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignUpSuccessful = true,
                        errorMessage = null,
                    )
                }
                // TODO: Handle email and username already exist error
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignUpSuccessful = false,
                        errorMessage = "Terjadi masalah. Silakan coba lagi nanti"
                    )

                    _snackbarEvent.send(_uiState.value.errorMessage!!)
                }
        }
    }
}