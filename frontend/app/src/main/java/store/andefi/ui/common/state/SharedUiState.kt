package store.andefi.ui.common.state

import store.andefi.data.local.entity.Account

data class SharedUiState(
    val account: Account? = null,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
