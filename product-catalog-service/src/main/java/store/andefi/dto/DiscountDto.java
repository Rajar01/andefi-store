package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record DiscountDto(
        String id,
        @JsonProperty("discount_percentage") Long discountPercentage,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("start_date") Instant startDate,
        @JsonProperty("end_date") Instant endDate
) {
}
