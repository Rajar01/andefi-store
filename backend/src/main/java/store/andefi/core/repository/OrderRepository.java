package store.andefi.core.repository;

import store.andefi.core.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findOrderByIdOptional(UUID orderId);

    List<Order> findAccountOrders(UUID accountId, String orderStatus, String period);

    boolean isOrderContainsOrderItem(UUID orderId, UUID orderItemId);
}
