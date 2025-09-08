package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProductReviewCatalogCursorPage {
    List<ProductReviewCatalogItem> reviews;
    String cursor;
    boolean hasMore;
}
