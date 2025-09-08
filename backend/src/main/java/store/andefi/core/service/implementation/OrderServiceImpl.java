package store.andefi.core.service.implementation;

import com.midtrans.httpclient.error.MidtransError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.*;
import store.andefi.core.enums.OrderStatus;
import store.andefi.core.enums.TransactionStatus;
import store.andefi.core.exception.*;
import store.andefi.core.model.OrderCatalog;
import store.andefi.core.model.OrderItemSummary;
import store.andefi.core.model.OrderSummary;
import store.andefi.core.repository.OrderRepository;
import store.andefi.core.service.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {
    @Inject
    ProductService productService;

    @Inject
    StockService stockService;

    @Inject
    PaymentService paymentService;

    @Inject
    ShippingAddressService shippingAddressService;

    @Inject
    CartService cartService;

    @Inject
    OrderRepository orderRepository;

    private Long calculateOrderSubTotal(List<OrderItem> orderItems) {
        return orderItems.stream().mapToLong(it -> it.getQuantity() * it.getProduct().getPrice()).reduce(0L, Long::sum);
    }

    private long calculateOrderTotalDiscount(List<OrderItem> orderItems) {
        return orderItems.stream().mapToLong(it -> Math.round(it.getQuantity() * ((double) (it.getProduct().getDiscountPercentage() * it.getProduct().getPrice()) / 100))).reduce(0L, Long::sum);
    }

    @Override
    public Order checkout(UUID accountId, List<OrderItem> orderItems) throws CheckoutOrderException, ProductStockCheckException {
        for (OrderItem orderItem : orderItems) {
            Optional<Product> productOptional = productService.getProductByIdOptional(orderItem.getProduct().getId());

            if (productOptional.isEmpty()) {
                String message = String.format("Product with id %s not found", orderItem.getProduct().getId().toString());
                throw new CheckoutOrderException(message);
            }

            if (!productService.isProductStockSufficient(orderItem.getProduct().getId(), orderItem.getQuantity())) {
                String message = String.format("Product with id %s is out of stock", orderItem.getProduct().getId());
                throw new CheckoutOrderException(message);
            }

            orderItem.setProduct(productOptional.get());
        }

        Long subTotal = calculateOrderSubTotal(orderItems);
        Long totalDiscount = calculateOrderTotalDiscount(orderItems);
        Long grandTotal = subTotal - totalDiscount;

        ShippingAddress shippingAddress = shippingAddressService.getShippingAddressByAccountIdOptional(accountId).orElse(null);

        return Order.builder()
            .orderItems(orderItems)
            .shippingAddress(shippingAddress)
            .subTotal(subTotal)
            .grandTotal(grandTotal)
            .totalDiscount(totalDiscount)
            .build();
    }


    @Override
    @Transactional
    public Order createOrder(UUID accountId, List<OrderItem> orderItems) throws OrderCreationException, MidtransError {
        for (OrderItem orderItem : orderItems) {
            Optional<Product> productOptional = productService.getProductByIdOptional(orderItem.getProduct().getId());

            if (productOptional.isEmpty()) {
                String message = String.format("Product with id %s not found", orderItem.getProduct().getId().toString());
                throw new OrderCreationException(message);
            }

            if (!productService.isProductStockSufficient(orderItem.getProduct().getId(), orderItem.getQuantity())) {
                String message = String.format("Product with id %s is out of stock", orderItem.getProduct().getId());
                throw new OrderCreationException(message);
            }

            orderItem.setProduct(productOptional.get());
        }


        Long subTotal = calculateOrderSubTotal(orderItems);
        Long totalDiscount = calculateOrderTotalDiscount(orderItems);
        Long grandTotal = subTotal - totalDiscount;

        ShippingAddress shippingAddress = shippingAddressService.getShippingAddressByAccountIdOptional(accountId)
            .orElseThrow(() -> new OrderCreationException("Shipping address can not be null"));

        // Save order to get order id
        Order order = orderRepository.save(Order.builder()
            .accountId(accountId)
            .orderItems(orderItems)
            .shippingAddress(shippingAddress)
            .orderStatus(OrderStatus.UNPAID)
            .subTotal(subTotal)
            .grandTotal(grandTotal)
            .totalDiscount(totalDiscount)
            .build()
        );

        order.getOrderItems().forEach(it -> {
            // Remove all ordered products from the cart
            cartService.removeProductFromCart(accountId, it.getProduct().getId());

            // Update product stock
            Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                .orElseThrow(() -> new OrderCreationException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

            stock.setAvailableQuantity(stock.getAvailableQuantity() - it.getQuantity());
            stock.setReservedQuantity(stock.getReservedQuantity() + it.getQuantity());

            stockService.updateStockByProductId(it.getProduct().getId(), stock);
        });

        // Set payment URL and update the order
        String paymentUrl = paymentService.generatePaymentUrl(order);
        order.setPaymentUrl(paymentUrl);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderByIdOptional(UUID orderId) {
        return orderRepository.findOrderByIdOptional(orderId);
    }

    @Override
    @Transactional
    public void processSuccessfulPayment(Order order, String paymentMethod, String currency) throws StockException {
        // Only process if order status paid or unpaid otherwise skip
        if (order.getOrderStatus() != OrderStatus.UNPAID && order.getOrderStatus() != OrderStatus.PAID) return;

        if (order.getOrderStatus() == OrderStatus.UNPAID) {
            // Update product stock
            order.getOrderItems().forEach(it -> {
                Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                    .orElseThrow(() -> new StockException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

                stock.setQuantityOnHand(stock.getQuantityOnHand() - it.getQuantity());
                stock.setAvailableQuantity(stock.getAvailableQuantity());
                stock.setReservedQuantity(stock.getReservedQuantity() - it.getQuantity());
                stock.setSoldQuantity(stock.getSoldQuantity() + it.getQuantity());

                stockService.updateStockByProductId(it.getProduct().getId(), stock);
            });

            // Update order information related to payment
            order.setOrderStatus(OrderStatus.PAID);
            order.setPaymentCurrency(currency);
            order.setPaymentMethod(paymentMethod);
            order.setPaidAt(Instant.now());
            order.setTransactionStatus(TransactionStatus.CAPTURE);
        }

        if (order.getOrderStatus() == OrderStatus.PAID) order.setTransactionStatus(TransactionStatus.SETTLEMENT);

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void processExpirePayment(Order order) throws StockException {
        // Update product stock
        order.getOrderItems().forEach(it -> {
            Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                .orElseThrow(() -> new StockException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

            stock.setAvailableQuantity(stock.getAvailableQuantity() + it.getQuantity());
            stock.setReservedQuantity(stock.getReservedQuantity() - it.getQuantity());

            stockService.updateStockByProductId(it.getProduct().getId(), stock);
        });

        // Update order information related to payment
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setTransactionStatus(TransactionStatus.EXPIRE);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Order order) throws OrderCancellationException, StockException {
        if (order.getOrderStatus() == OrderStatus.REFUNDED) {
            String message = String.format("Order with id %s can not be canceled because it already refunded", order.getId().toString());
            throw new OrderCancellationException(message);
        }

        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            String message = String.format("Order with id %s can not be canceled because it already completed", order.getId().toString());
            throw new OrderCancellationException(message);
        }

        if (order.getOrderStatus() == OrderStatus.PACKED || order.getOrderStatus() == OrderStatus.SHIPPED) {
            throw new OrderCancellationException("Order can not be canceled because it going to be shipped soon");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELED) return;

        // Update product stock quantities when an unpaid order is canceled
        if (order.getOrderStatus() == OrderStatus.UNPAID) {
            order.getOrderItems().forEach(it -> {
                Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                    .orElseThrow(() -> new StockException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

                stock.setAvailableQuantity(stock.getAvailableQuantity() + it.getQuantity());
                stock.setReservedQuantity(stock.getReservedQuantity() - it.getQuantity());

                stockService.updateStockByProductId(it.getProduct().getId(), stock);
            });
        }

        // Update product stock quantities when a paid order is canceled
        if (order.getOrderStatus() == OrderStatus.PAID) {
            order.getOrderItems().forEach(it -> {
                Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                    .orElseThrow(() -> new StockException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

                stock.setQuantityOnHand(stock.getQuantityOnHand() + it.getQuantity());
                stock.setAvailableQuantity(stock.getAvailableQuantity() + it.getQuantity());
                stock.setSoldQuantity(stock.getSoldQuantity() - it.getQuantity());

                stockService.updateStockByProductId(it.getProduct().getId(), stock);
            });
        }


        // Update order information related to payment
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setTransactionStatus(TransactionStatus.CANCEL);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void refundOrder(Order order, boolean partial) throws OrderRefundException, StockException {
        if (order.getOrderStatus() == OrderStatus.REFUNDED) return;

        // Skip stock update if order status already refunded or partial refunded
        if (!partial || order.getOrderStatus() != OrderStatus.PARTIAL_REFUNDED) {
            order.getOrderItems().forEach(it -> {
                Stock stock = stockService.getStockByProductIdOptional(it.getProduct().getId())
                    .orElseThrow(() -> new StockException(String.format("Product stock not found for product with id %s", it.getProduct().getId())));

                stock.setQuantityOnHand(stock.getQuantityOnHand() + it.getQuantity());
                stock.setAvailableQuantity(stock.getAvailableQuantity() + it.getQuantity());
                stock.setSoldQuantity(stock.getSoldQuantity() - it.getQuantity());

                stockService.updateStockByProductId(it.getProduct().getId(), stock);
            });
        }

        if (partial) {
            order.setTransactionStatus(TransactionStatus.PARTIAL_REFUND);
            order.setOrderStatus(OrderStatus.PARTIAL_REFUNDED);
        } else {
            order.setTransactionStatus(TransactionStatus.REFUND);
            order.setOrderStatus(OrderStatus.REFUNDED);
        }

        orderRepository.save(order);
    }

    @Override
    public OrderCatalog getOrderCatalogByAccountId(UUID accountId, String orderStatus, String period) throws OrderCatalogException {
        if (accountId == null) {
            throw new OrderCatalogException("Product id can not be blank or null");
        }

        List<Order> orders = orderRepository.findAccountOrders(accountId, orderStatus, period);

        // Convert order domain entity into order summary model
        List<OrderSummary> orderSummaries = orders.stream().map(order -> {
            List<OrderItemSummary> orderItemSummaries = order.getOrderItems().stream().map(orderItem -> OrderItemSummary.builder()
                .id(orderItem.getId())
                .productName(orderItem.getProduct().getName())
                .productPrice(orderItem.getProduct().getPrice())
                .productDiscountPercentage(orderItem.getProduct().getDiscountPercentage())
                .productMediaUrl(orderItem.getProduct().getMediaUrls().get("primary_image"))
                .quantity(orderItem.getQuantity())
                .build()
            ).toList();

            return OrderSummary.builder()
                .id(order.getId())
                .orderItemSummaries(orderItemSummaries)
                .orderStatus(order.getOrderStatus())
                .grandTotal(order.getGrandTotal())
                .paymentUrl(order.getPaymentUrl())
                .purchaseAt(order.getPurchaseAt())
                .build();
        }).toList();

        return OrderCatalog.builder().accountId(accountId).orderSummaries(orderSummaries).build();
    }

    @Override
    public boolean isOrderItemBelongsToOrder(UUID orderId, UUID orderItemId) {
        return orderRepository.isOrderContainsOrderItem(orderId, orderItemId);
    }
}
