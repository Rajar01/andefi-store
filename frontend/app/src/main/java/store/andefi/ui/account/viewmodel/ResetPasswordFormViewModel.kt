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
import store.andefi.ui.account.state.ResetPasswordFormUiState
import javax.inject.Inject

@HiltViewModel
class ResetPasswordFormViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordFormUiState())
    val uiState: StateFlow<ResetPasswordFormUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun onPasswordValueChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onPasswordConfirmationValueChanged(passwordConfirmation: String) {
        _uiState.value = _uiState.value.copy(passwordConfirmation = passwordConfirmation)
    }

    fun resetPassword(token: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val password = _uiState.value.password
            val passwordConfirmation = _uiState.value.passwordConfirmation

            // TODO: Handle password validation

            accountService.resetPassword(token, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isResetPasswordSuccessful = true,
                        errorMessage = null,
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isResetPasswordSuccessful = false,
                        errorMessage = "Terjadi masalah. Silakan coba lagi nanti"
                    )

                    _snackbarEvent.send(_uiState.value.errorMessage!!)
                }
        }
    }
}