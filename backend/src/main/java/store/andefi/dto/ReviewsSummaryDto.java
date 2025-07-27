package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ReviewsSummaryDto {
  @JsonProperty("product_id")
  private String productId;

  @JsonProperty("total_reviews")
  private long totalReviews;

  @JsonProperty("total_ratings")
  private long totalRatings;

  @JsonProperty("average_ratings")
  private float averageRatings;
}
