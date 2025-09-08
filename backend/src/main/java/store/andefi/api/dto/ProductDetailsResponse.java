package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailsResponse {
    UUID id;

    String name;

    String description;

    Long price;

    @JsonProperty("discount_percentage")
    Integer discountPercentage;

    Map<String, String> attributes;

    @JsonProperty("media_urls")
    Map<String, String> mediaUrls;

    @JsonProperty("sold_quantity")
    Integer soldQuantity;

    @JsonProperty("total_reviews")
    Integer totalReviews;

    @JsonProperty("total_ratings")
    Integer totalRatings;

    @JsonProperty("average_rating")
    Float averageRating;

    @JsonProperty("latest_review")
    LatestReviewResponse latestReview;
}
