package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import store.andefi.core.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderSummaryResponse {
    private UUID id;

    @JsonProperty("order_item_summaries")
    private List<OrderItemSummaryResponse> orderItemSummaries;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("grand_total")
    private Long grandTotal;

    @JsonProperty("payment_url")
    private String paymentUrl;

    @JsonProperty("purchase_at")
    private Instant purchaseAt;
}
