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
public class GetOrCreateCartResponse {
    private UUID id;

    @JsonProperty("cart_items")
    private List<CartItemResponse> cartItems;
}
