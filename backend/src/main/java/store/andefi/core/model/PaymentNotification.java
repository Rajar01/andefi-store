package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentNotification {
    String signatureKey;
    String transactionId;
    String transactionStatus;
    String transactionTime;
    String orderId;
    String paymentType;
    String currency;
    String statusCode;
    String grossAmount;
    String fraudStatus;
}
