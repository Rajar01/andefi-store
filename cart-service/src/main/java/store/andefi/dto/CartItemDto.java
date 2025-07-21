package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartItemDto(@JsonProperty("product_id") String productId, Long quantity) {}
