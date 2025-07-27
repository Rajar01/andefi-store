package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentNotificationDto {
  @JsonProperty("signature_key")
  private String signatureKey;

  @JsonProperty("transaction_id")
  private String transactionId;

  @JsonProperty("transaction_status")
  private String transactionStatus;

  @JsonProperty("transaction_time")
  private String transactionTime;

  @JsonProperty("order_id")
  private String orderId;

  @JsonProperty("payment_type")
  private String paymentType;

  private String currency;

  @JsonProperty("gross_amount")
  private String grossAmount;

  @JsonProperty("fraud_status")
  private String fraudStatus;
}
