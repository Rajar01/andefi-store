package store.andefi.ui.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import store.andefi.data.remote.dto.OrderResponseDto
import store.andefi.data.repository.OrderRepository
import store.andefi.ui.order.state.OrderHistoryUiState
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrderHistoryUiState>(OrderHistoryUiState.Empty)
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()
    
    fun init(token: String) {
        viewModelScope.launch {
            _uiState.value = OrderHistoryUiState.Loading

            try {
                val ordersDeferred = async { getOrders(token) }

                val orders = ordersDeferred.await()

                if (orders.isNotEmpty()) {
                    _uiState.value = OrderHistoryUiState.Success(
                        orders = orders
                    )
                } else {
                    _uiState.value = OrderHistoryUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = OrderHistoryUiState.Error
            }
        }
    }

    suspend fun getOrders(token: String): List<OrderResponseDto> {
        return orderRepository.getOrders(token).getOrThrow()
    }
}