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
import store.andefi.data.repository.AccountRepository
import store.andefi.ui.account.state.ForgotPasswordEmailConfirmationUiState
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordEmailConfirmationViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordEmailConfirmationUiState())
    val uiState: StateFlow<ForgotPasswordEmailConfirmationUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun resendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            accountRepository.sendPasswordResetEmail(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                    )

                    _snackbarEvent.send("Tautan pengaturan ulang kata sandi berhasil dikirim ulang. Silakan periksa kembali email Anda.")
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Terjadi masalah. Silakan coba lagi nanti"
                    )

                    _snackbarEvent.send(_uiState.value.errorMessage!!)
                }
        }
    }
}