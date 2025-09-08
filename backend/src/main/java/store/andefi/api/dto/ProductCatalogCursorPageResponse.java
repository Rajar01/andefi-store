package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import store.andefi.core.model.ProductSummary;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductCatalogCursorPageResponse {
    @JsonProperty("product_summaries")
    List<ProductSummary> productSummaries;

    String cursor;

    @JsonProperty("has_more")
    boolean hasMore;
}
