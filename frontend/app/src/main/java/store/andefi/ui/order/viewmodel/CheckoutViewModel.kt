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
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.local.dao.PaymentDao
import store.andefi.data.local.entity.Payment
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.data.remote.dto.ShippingAddressResponseDto
import store.andefi.data.repository.OrderRepository
import store.andefi.data.repository.ProductRepository
import store.andefi.data.repository.ShippingAddressRepository
import store.andefi.ui.common.navigation.CheckoutRoute
import store.andefi.ui.common.navigation.navtype.OrderCheckoutRequestDtoNavType
import store.andefi.ui.order.state.CheckoutUiState
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val shippingAddressRepository: ShippingAddressRepository,
    private val paymentDao: PaymentDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args =
        savedStateHandle.toRoute<CheckoutRoute>(typeMap = mapOf(typeOf<OrderCheckoutRequestDto>() to OrderCheckoutRequestDtoNavType))
    private val _uiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun init(token: String) {
        viewModelScope.launch {
            _uiState.value = CheckoutUiState.Loading

            try {
                val shippingAddressDeferred = async { getAccountShippingAddress(token) }
                val productsDeferred = async { getProducts() }

                val shippingAddress = shippingAddressDeferred.await()
                val products = productsDeferred.await()

                _uiState.value = CheckoutUiState.Success(
                    shippingAddressResponseDto = shippingAddress,
                    products = products,
                    orderCheckoutRequestDto = args.orderCheckoutRequestDto,
                )
            } catch (e: Exception) {
                _uiState.value = CheckoutUiState.Error
            }
        }
    }

    suspend fun getAccountShippingAddress(token: String): ShippingAddressResponseDto {
        return shippingAddressRepository.getAccountShippingAddress(token).getOrThrow()
    }

    suspend fun getProducts(): List<ProductResponseDto> {
        val products = mutableListOf<ProductResponseDto>()

        for (item in args.orderCheckoutRequestDto.orderItem) {
            val product = productRepository.getProductById(item.productId).getOrThrow()
            products.add(product)
        }

        return products
    }


    fun checkoutOrder(
        token: String,
        openUri: (String) -> Unit,
        navigateToOrderDetailRoute: (String) -> Unit
    ) {
        viewModelScope.launch {
            orderRepository.checkoutOrder(token, args.orderCheckoutRequestDto)
                .onSuccess {
                    paymentDao.insertPayment(Payment(it.orderId, it.paymentRedirectUrl))

                    navigateToOrderDetailRoute(it.orderId)

                    openUri(it.paymentRedirectUrl)
                }
                .onFailure { _uiState.value = CheckoutUiState.Error }
        }
    }
}