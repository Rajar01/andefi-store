package store.andefi.core.service;

import com.midtrans.httpclient.error.MidtransError;
import store.andefi.core.entity.Order;
import store.andefi.core.entity.OrderItem;
import store.andefi.core.exception.*;
import store.andefi.core.model.OrderCatalog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Order checkout(UUID accountId, List<OrderItem> orderItems) throws CheckoutOrderException, ProductStockCheckException;

    Order createOrder(UUID accountId, List<OrderItem> orderItems) throws OrderCreationException, GeneratePaymentUrlException, MidtransError;

    Optional<Order> getOrderByIdOptional(UUID orderId);

    void processSuccessfulPayment(Order order, String paymentMethod, String currency) throws StockException;

    void processExpirePayment(Order order) throws StockException;

    void cancelOrder(Order order) throws OrderCancellationException, StockException;

    void refundOrder(Order order, boolean partial) throws OrderRefundException, StockException;

    OrderCatalog getOrderCatalogByAccountId(UUID accountId, String orderStatus, String period) throws OrderCatalogException;

    boolean isOrderItemBelongsToOrder(UUID orderId, UUID orderItemId);
}
