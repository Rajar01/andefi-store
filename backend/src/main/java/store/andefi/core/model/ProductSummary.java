package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ProductSummary {
    UUID id;
    String name;
    String description;
    Long price;
    Integer discountPercentage;
    String mediaUrl;
    Integer soldQuantity;
    Float averageRating;
}
