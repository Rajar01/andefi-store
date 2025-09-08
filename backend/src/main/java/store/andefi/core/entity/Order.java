package store.andefi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.andefi.core.enums.OrderStatus;
import store.andefi.core.enums.TransactionStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID id;
    private UUID accountId;
    private List<OrderItem> orderItems;
    private ShippingAddress shippingAddress;
    private OrderStatus orderStatus;
    private TransactionStatus transactionStatus;
    private Long subTotal;
    private Long grandTotal;
    private Long totalDiscount;
    private String paymentCurrency;
    private String paymentMethod;
    private String paymentUrl;
    private Instant purchaseAt;
    private Instant paidAt;
    private Instant shippingAt;
    private Instant completedAt;
}
