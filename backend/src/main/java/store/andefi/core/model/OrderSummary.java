package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;
import store.andefi.core.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class OrderSummary {
    UUID id;
    List<OrderItemSummary> orderItemSummaries;
    OrderStatus orderStatus;
    Long grandTotal;
    String paymentUrl;
    Instant purchaseAt;
}
