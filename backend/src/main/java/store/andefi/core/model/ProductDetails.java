package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class ProductDetails {
    UUID id;
    String name;
    String description;
    Long price;
    Integer discountPercentage;
    Map<String, String> attributes;
    Map<String, String> mediaUrls;
    Integer soldQuantity;
    Integer totalReviews;
    Integer totalRatings;
    Float averageRating;
    LatestReview latestReview;
}
