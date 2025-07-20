package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockUpdateDto(
        @JsonProperty("product_id")
        String productId,
        @JsonProperty("quantity_on_hand")
        Long quantityOnHand,
        @JsonProperty("reserved_quantity")
        Long reservedQuantity
) {
}
