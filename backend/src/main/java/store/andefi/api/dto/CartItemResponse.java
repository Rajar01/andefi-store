package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CartItemResponse {
    private UUID id;

    private Integer quantity;

    @JsonProperty("product_id")
    private UUID productId;
}
