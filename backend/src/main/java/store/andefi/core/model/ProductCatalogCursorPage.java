package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProductCatalogCursorPage {
    List<ProductSummary> productSummaries;
    String cursor;
    boolean hasMore;
}
