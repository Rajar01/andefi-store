package store.andefi.ui.common.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import store.andefi.data.repository.AccountRepository
import store.andefi.ui.common.state.SharedUiState
import javax.inject.Inject

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    init {
        getAccountInformation()
    }

    fun getAccountInformation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

            accountRepository.getAccountInformation()
                .onSuccess {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, isError = false, account = it)
                }
                .onFailure {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, isError = true)
                }
        }
    }

    fun authenticate() {
        _uiState.value = _uiState.value.copy(isAuthenticated = true)
    }
}