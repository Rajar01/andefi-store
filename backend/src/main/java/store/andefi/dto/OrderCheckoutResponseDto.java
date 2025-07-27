package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderCheckoutResponseDto {
  @JsonProperty("order_id")
  private String orderId;

  @JsonProperty("transaction_token")
  private String transactionToken;

  @JsonProperty("total_amount_before_discount")
  private long totalAmountBeforeDiscount;

  @JsonProperty("total_amount_after_discount")
  private long totalAmountAfterDiscount;

  @JsonProperty("total_discount")
  private long totalDiscount;
}
