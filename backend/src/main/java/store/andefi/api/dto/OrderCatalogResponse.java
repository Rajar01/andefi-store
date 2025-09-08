package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderCatalogResponse {
    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("order_summaries")
    private List<OrderSummaryResponse> orderSummaries;
}
