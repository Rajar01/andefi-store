package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockDto(
        @JsonProperty("product_id")
        String productId,
        @JsonProperty("quantity_on_hand")
        Long quantityOnHand,
        @JsonProperty("reserved_quantity")
        Long reservedQuantity,
        @JsonProperty("available_quantity")
        Long availableQuantity
) {
}
