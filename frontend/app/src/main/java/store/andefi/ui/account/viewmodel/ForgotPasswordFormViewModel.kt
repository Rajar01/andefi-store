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
import store.andefi.service.AccountService
import store.andefi.ui.account.state.ForgotPasswordFormUiState
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordFormViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordFormUiState())
    val uiState: StateFlow<ForgotPasswordFormUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()


    fun onEmailValueChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun sendPasswordResetEmail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // TODO: Validate forgot password form input
            val email = _uiState.value.email

            accountService.sendPasswordResetEmail(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSendPasswordResetEmailSuccessful = true,
                        errorMessage = null,
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSendPasswordResetEmailSuccessful = false,
                        errorMessage = "Terjadi masalah. Silakan coba lagi nanti"
                    )

                    _snackbarEvent.send(_uiState.value.errorMessage!!)
                }
        }
    }
}