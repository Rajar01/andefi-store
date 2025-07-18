package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ReviewsDto(
        List<ReviewDto> reviews,
        @JsonProperty("has_more") boolean hasMore,
        @JsonProperty("next_cursor") String nextCursor
) {
}
