package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderItemSummary {
    UUID id;
    String productName;
    Long productPrice;
    Integer productDiscountPercentage;
    String productMediaUrl;
    Integer quantity;
}
