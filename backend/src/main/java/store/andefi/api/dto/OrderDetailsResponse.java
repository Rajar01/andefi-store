package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import store.andefi.core.enums.OrderStatus;
import store.andefi.core.enums.TransactionStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderDetailsResponse {
    private UUID id;

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("order_items")
    private List<OrderItemDetailsResponse> orderItems;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_postal_code")
    private String shippingPostalCode;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;

    @JsonProperty("sub_total")
    private Long subTotal;

    @JsonProperty("grand_total")
    private Long grandTotal;

    @JsonProperty("total_discount")
    private Long totalDiscount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_url")
    private String paymentUrl;

    @JsonProperty("purchase_at")
    private Instant purchaseAt;

    @JsonProperty("paid_at")
    private Instant paidAt;

    @JsonProperty("shipping_at")
    private Instant shippingAt;

    @JsonProperty("completed_at")
    private Instant completedAt;
}
