package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;

public record DiscountDto(
        ObjectId id,
        @JsonProperty("discount_percentage") Long discountPercentage,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("start_date") Instant startDate,
        @JsonProperty("end_date") Instant endDate
) {
}
