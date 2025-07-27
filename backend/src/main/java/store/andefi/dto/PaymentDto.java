package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Currency;
import lombok.Data;

@Data
public class PaymentDto {
  private String id;

  @JsonProperty("account_id")
  private String accountId;

  @JsonProperty("order_id")
  private String orderId;

  private String method;

  private Currency currency;

  private long amount;

  @JsonProperty("transaction_id")
  private String transactionId;

  @JsonProperty("transaction_time")
  private String transactionTime;

  @JsonProperty("transaction_status")
  private String transactionStatus;
}
