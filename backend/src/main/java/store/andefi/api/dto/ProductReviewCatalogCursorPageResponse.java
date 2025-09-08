package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductReviewCatalogCursorPageResponse {
    List<ProductReviewCatalogItemResponse> reviews;

    String cursor;

    @JsonProperty("has_more")
    boolean hasMore;
}
