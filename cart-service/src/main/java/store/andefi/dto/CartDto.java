package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CartDto(String id, @JsonProperty("cart_items") List<CartItemDto> cartItems) {}
