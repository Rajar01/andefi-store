package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class OrderCatalog {
    UUID accountId;
    List<OrderSummary> orderSummaries;
}
