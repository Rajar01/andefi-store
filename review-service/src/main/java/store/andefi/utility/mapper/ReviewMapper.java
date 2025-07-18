package store.andefi.utility.mapper;

import store.andefi.dto.ReviewDto;
import store.andefi.entity.Review;

public final class ReviewMapper {
  public static ReviewDto toDto(Review review) {
    return new ReviewDto(
        review.id().toString(),
        review.productId(),
        review.content(),
        review.rating(),
        null,
        null,
        review.createdAt());
  }
}
