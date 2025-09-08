package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Order;
import store.andefi.core.entity.OrderItem;
import store.andefi.core.entity.Product;
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.enums.OrderPeriod;
import store.andefi.core.enums.OrderStatus;
import store.andefi.core.repository.OrderRepository;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderItemPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository, PanacheRepositoryBase<OrderPanacheEntity, UUID> {
    private List<OrderItemPanacheEntity> mapOrderItemDomainEntitiesIntoOrderItemPanacheEntities(List<OrderItem> orderItems, OrderPanacheEntity orderPanacheEntity) {
        return orderItems.stream().map(it -> {
            ProductPanacheEntity productPanacheEntity = getEntityManager()
                .getReference(ProductPanacheEntity.class, it.getProduct().getId());

            return OrderItemPanacheEntity.builder()
                .id(it.getId())
                .productName(productPanacheEntity.getName())
                .productPrice(productPanacheEntity.getPrice())
                .productDiscountPercentage(productPanacheEntity.getDiscountPercentage())
                .productMediaUrl(productPanacheEntity.getMediaUrls().get("primary_image"))
                .quantity(it.getQuantity())
                .product(productPanacheEntity)
                .order(orderPanacheEntity)
                .build();
        }).toList();
    }

    // Convert panache entity into core entity (domain entity)
    private Order toDomainEntity(OrderPanacheEntity orderPanacheEntity) {
        ShippingAddress shippingAddress = ShippingAddress.builder()
            .address(orderPanacheEntity.getShippingAddress())
            .postalCode(orderPanacheEntity.getShippingPostalCode())
            .build();

        List<OrderItem> orderItems = orderPanacheEntity.getOrderItems().stream().map(it -> {
            // TODO: Add guard to prevent if product is null when get the product id
            Product product = Product.builder()
                .id(it.getProduct().getId())
                .name(it.getProductName())
                .price(it.getProductPrice())
                .discountPercentage(it.getProductDiscountPercentage())
                .mediaUrls(Map.of("primary_image", it.getProductMediaUrl()))
                .build();

            return OrderItem.builder()
                .id(it.getId())
                .product(product)
                .quantity(it.getQuantity())
                .build();
        }).toList();

        return Order.builder()
            .id(orderPanacheEntity.getId())
            .accountId(orderPanacheEntity.getAccount().getId())
            .orderItems(orderItems)
            .shippingAddress(shippingAddress)
            .orderStatus(orderPanacheEntity.getOrderStatus())
            .transactionStatus(orderPanacheEntity.getTransactionStatus())
            .subTotal(orderPanacheEntity.getSubTotal())
            .grandTotal(orderPanacheEntity.getGrandTotal())
            .totalDiscount(orderPanacheEntity.getTotalDiscount())
            .paymentCurrency(orderPanacheEntity.getPaymentCurrency())
            .paymentMethod(orderPanacheEntity.getPaymentMethod())
            .paymentUrl(orderPanacheEntity.getPaymentUrl())
            .purchaseAt(orderPanacheEntity.getCreatedAt())
            .paidAt(orderPanacheEntity.getPaidAt())
            .shippingAt(orderPanacheEntity.getShippingAt())
            .completedAt(orderPanacheEntity.getCompletedAt())
            .build();
    }

    // Use this function when created new record only
    private OrderPanacheEntity toPanacheEntity(Order order) {
        OrderPanacheEntity orderPanacheEntity = OrderPanacheEntity.builder()
            .id(order.getId())
            .shippingAddress(order.getShippingAddress().getAddress())
            .shippingPostalCode(order.getShippingAddress().getPostalCode())
            .orderStatus(order.getOrderStatus())
            .transactionStatus(order.getTransactionStatus())
            .subTotal(order.getSubTotal())
            .grandTotal(order.getGrandTotal())
            .totalDiscount(order.getTotalDiscount())
            .build();

        // Attach account reference into order panache entity
        AccountPanacheEntity accountPanacheEntity = getEntityManager().getReference(AccountPanacheEntity.class, order.getAccountId());
        orderPanacheEntity.setAccount(accountPanacheEntity);

        // Map order items domain entity into order item panache entity and set relationship
        List<OrderItemPanacheEntity> orderItemPanacheEntities = mapOrderItemDomainEntitiesIntoOrderItemPanacheEntities(order.getOrderItems(), orderPanacheEntity);
        orderPanacheEntity.setOrderItems(orderItemPanacheEntities);

        return orderPanacheEntity;
    }

    @Override
    public Optional<Order> findOrderByIdOptional(UUID orderId) {
        return findByIdOptional(orderId).map(this::toDomainEntity);
    }

    @Override
    public Order save(Order order) {
        OrderPanacheEntity orderPanacheEntity = getEntityManager()
            .createQuery("FROM OrderPanacheEntity p WHERE p.id = :id", OrderPanacheEntity.class)
            .setParameter("id", order.getId())
            .getSingleResultOrNull();

        // Update existing order
        if (orderPanacheEntity != null) {
            orderPanacheEntity.setOrderStatus(order.getOrderStatus());
            orderPanacheEntity.setTransactionStatus(order.getTransactionStatus());
            orderPanacheEntity.setPaymentCurrency(order.getPaymentCurrency());
            orderPanacheEntity.setPaymentMethod(order.getPaymentMethod());
            orderPanacheEntity.setPaymentUrl(order.getPaymentUrl());
            orderPanacheEntity.setPaidAt(order.getPaidAt());
            orderPanacheEntity.setShippingAt(order.getShippingAt());
            orderPanacheEntity.setCompletedAt(order.getCompletedAt());

            return toDomainEntity(orderPanacheEntity);
        }

        // Create new order
        OrderPanacheEntity newOrderPanacheEntity = toPanacheEntity(order);
        persist(newOrderPanacheEntity);
        return toDomainEntity(newOrderPanacheEntity);
    }

    @Override
    public List<Order> findAccountOrders(UUID accountId, String orderStatus, String period) {
        // Build base query and parameters
        StringBuilder query = new StringBuilder("account.id = ?1");
        List<Object> params = new ArrayList<>();
        params.add(accountId);

        // Add order status filter if present
        if (orderStatus != null && !orderStatus.isBlank()) {
            query.append(" and orderStatus = ?").append(params.size() + 1);
            params.add(OrderStatus.valueOf(orderStatus));
        }

        // Add order period filter if present
        // TODO: Refactor this code later
        if (period != null && !period.isBlank()) {
            Instant now = Instant.now();
            if (period.equalsIgnoreCase(OrderPeriod.LAST_30_DAYS.toString())) {
                query.append(" and (createdAt >= ?").append(params.size() + 1);
                query.append(" and createdAt <= ?").append(params.size() + 2).append(")");
                params.add(now.minus(Duration.ofDays(30)));
                params.add(now);
            }

            if (period.equalsIgnoreCase(OrderPeriod.LAST_60_DAYS.toString())) {
                query.append(" and (createdAt >= ?").append(params.size() + 1);
                query.append(" and createdAt <= ?").append(params.size() + 2).append(")");
                params.add(now.minus(Duration.ofDays(60)));
                params.add(now);
            }

            if (period.equalsIgnoreCase(OrderPeriod.LAST_90_DAYS.toString())) {
                query.append(" and (createdAt >= ?").append(params.size() + 1);
                query.append(" and createdAt <= ?").append(params.size() + 2).append(")");
                params.add(now.minus(Duration.ofDays(90)));
                params.add(now);
            }
        }

        List<OrderPanacheEntity> orderPanacheEntities = find(query.toString(), params.toArray()).list();

        return orderPanacheEntities.stream().map(this::toDomainEntity).toList();
    }

    @Override
    public boolean isOrderContainsOrderItem(UUID orderId, UUID orderItemId) {
        return findById(orderId).getOrderItems().stream().map(OrderItemPanacheEntity::getId).toList().contains(orderItemId);
    }
}
