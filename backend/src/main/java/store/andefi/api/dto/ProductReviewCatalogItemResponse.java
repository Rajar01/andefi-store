package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ProductReviewCatalogItemResponse {
    @JsonProperty("reviewer_name")
    String reviewerName;

    @JsonProperty("review_at")
    Instant reviewAt;

    String content;

    Integer rating;
}
