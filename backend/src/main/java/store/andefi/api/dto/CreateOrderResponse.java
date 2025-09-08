package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateOrderResponse {
    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("payment_url")
    private String paymentUrl;
}
