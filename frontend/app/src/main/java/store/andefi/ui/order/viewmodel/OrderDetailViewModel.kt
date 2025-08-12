package store.andefi.ui.order.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import store.andefi.data.local.dao.PaymentDao
import store.andefi.data.remote.dto.OrderResponseDto
import store.andefi.data.repository.OrderRepository
import store.andefi.ui.common.navigation.OrderDetailRoute
import store.andefi.ui.order.state.OrderDetailUiState
import store.andefi.ui.order.state.OrderHistoryUiState
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val paymentDao: PaymentDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<OrderDetailRoute>()

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()
    
    fun init(token: String) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading

            try {
                val orderDeferred = async { getOrder(token, args.orderId) }

                val order = orderDeferred.await()

                _uiState.value = OrderDetailUiState.Success(
                    order = order
                )

            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error
            }
        }
    }

    suspend fun getOrder(token: String, orderId: String): OrderResponseDto {
        return orderRepository.getOrder(token, orderId).getOrThrow()
    }

    fun pay(
        orderId: String,
        openUri: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val payment = paymentDao.getPayment(orderId)
            payment?.let {
                openUri(it.redirectUrl)
            }
        }
    }
}