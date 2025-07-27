package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
  private String id;

  @JsonProperty("account_id")
  private String accountId;

  @JsonProperty("order_items")
  private List<OrderItemDto> orderItems;

  private ShippingAddressDto shippingAddress;

  private PaymentDto payment;

  private String status;

  @JsonProperty("paid_at")
  private Instant paidAt;

  @JsonProperty("shipping_at")
  private Instant shippingAt;

  @JsonProperty("completed_at")
  private Instant completedAt;

  @JsonProperty("created_at")
  private Instant createdAt;
}
