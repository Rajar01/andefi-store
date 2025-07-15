package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record DiscountDto(
        @JsonProperty("id") UUID id,
        @JsonProperty("product_id") UUID productId,
        @JsonProperty("discount_percentage") Long discountPercentage,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("start_date") Instant startDate,
        @JsonProperty("end_date") Instant endDate
) {
}
