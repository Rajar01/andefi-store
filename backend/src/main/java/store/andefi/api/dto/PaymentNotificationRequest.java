package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentNotificationRequest {
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

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("gross_amount")
    private String grossAmount;

    @JsonProperty("fraud_status")
    private String fraudStatus;
}
