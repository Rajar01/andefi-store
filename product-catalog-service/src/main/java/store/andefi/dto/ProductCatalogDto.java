package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductCatalogDto(
        List<ProductDto> products,
        @JsonProperty("has_more") boolean hasMore,
        @JsonProperty("next_cursor") String nextCursor
) {
}
