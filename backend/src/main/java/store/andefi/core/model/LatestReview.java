package store.andefi.core.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class LatestReview {
    String reviewerName;
    Instant reviewAt;
    String content;
    Integer rating;
}
