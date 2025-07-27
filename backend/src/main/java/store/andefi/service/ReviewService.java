package store.andefi.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import store.andefi.dto.*;
import store.andefi.entity.Review;
import store.andefi.repository.ReviewRepository;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.SortBy;
import store.andefi.utility.mapper.ReviewMapper;

@ApplicationScoped
public class ReviewService {
  @Inject ReviewRepository reviewRepository;

  public ReviewsDto getProductReviews(
      String productId, int rating, String sortBy, int limit, String cursor) {
    List<Review> reviewEntities;

    if (limit < 1 || limit > 100) {
      throw new BadRequestException();
    }

    if (cursor == null || cursor.isBlank()) {
      if (rating == 0) {
        reviewEntities =
            reviewRepository
                .find(
                    "product.id",
                    Objects.equals(sortBy, SortBy.RATING_ASC.toString())
                        ? Sort.by("rating", Sort.Direction.Ascending)
                        : Objects.equals(sortBy, SortBy.RATING_DESC.toString())
                            ? Sort.by("rating", Sort.Direction.Descending)
                            : Sort.by("id", Sort.Direction.Ascending),
                    UUID.fromString(productId))
                .page(0, limit + 1)
                .list();
      } else {
        reviewEntities =
            reviewRepository
                .find(
                    "product.id = ?1 and rating = ?2",
                    Objects.equals(sortBy, SortBy.RATING_ASC.toString())
                        ? Sort.by("rating", Sort.Direction.Ascending)
                        : Objects.equals(sortBy, SortBy.RATING_DESC.toString())
                            ? Sort.by("rating", Sort.Direction.Descending)
                            : Sort.by("id", Sort.Direction.Ascending),
                    UUID.fromString(productId),
                    rating)
                .page(0, limit + 1)
                .list();
      }
    } else {
      UUID c = UUID.fromString(CursorCodec.decode(cursor));

      if (rating == 0) {
        reviewEntities =
            reviewRepository
                .find(
                    "product.id = ?1 and id >= ?2",
                    Objects.equals(sortBy, SortBy.RATING_ASC.toString())
                        ? Sort.by("rating", Sort.Direction.Ascending)
                        : Objects.equals(sortBy, SortBy.RATING_DESC.toString())
                            ? Sort.by("rating", Sort.Direction.Descending)
                            : Sort.by("id", Sort.Direction.Ascending),
                    UUID.fromString(productId),
                    c)
                .page(0, limit + 1)
                .list();
      } else {
        reviewEntities =
            reviewRepository
                .find(
                    "product.id = ?1 and id >= ?2 and rating = ?3",
                    Objects.equals(sortBy, SortBy.RATING_ASC.toString())
                        ? Sort.by("rating", Sort.Direction.Ascending)
                        : Objects.equals(sortBy, SortBy.RATING_DESC.toString())
                            ? Sort.by("rating", Sort.Direction.Descending)
                            : Sort.by("id", Sort.Direction.Ascending),
                    UUID.fromString(productId),
                    c,
                    rating)
                .page(0, limit + 1)
                .list();
      }
    }

    // Convert review entity into review dto
    List<ReviewDto> reviewDtos = reviewEntities.stream().map(ReviewMapper::toDto).toList();

    boolean hasMore = reviewDtos.size() > limit;
    String nextCursor = hasMore ? CursorCodec.encode(reviewDtos.getLast().getId()) : null;
    if (hasMore) reviewDtos = reviewDtos.subList(0, reviewDtos.size() - 1);

    return new ReviewsDto(reviewDtos, hasMore, nextCursor);
  }

  public ReviewsSummaryDto getProductReviewSummary(String productId) {
    List<Review> reviewEntities =
        reviewRepository.find("product.id", UUID.fromString(productId)).list();

    long totalReviews =
        reviewEntities.stream()
            .filter(reviewEntity -> !reviewEntity.getContent().isBlank())
            .count();

    long totalRating = reviewEntities.size();

    float averageRating =
        reviewEntities.stream().map(Review::getRating).reduce(0f, Float::sum) / (float) totalRating;

    ReviewsSummaryDto reviewsSummaryDto = new ReviewsSummaryDto();
    reviewsSummaryDto.setProductId(productId);
    reviewsSummaryDto.setTotalReviews(totalReviews);
    reviewsSummaryDto.setTotalRatings(totalRating);
    reviewsSummaryDto.setAverageRatings(averageRating);

    return reviewsSummaryDto;
  }

  public ReviewMediaDto getProductReviewMedia(String productId, int limit, String cursor) {
    List<Review> reviewEntities;

    if (limit < 1 || limit > 100) {
      throw new BadRequestException();
    }

    if (cursor == null || cursor.isBlank()) {
      reviewEntities =
          reviewRepository
              .find(
                  "product.id", Sort.by("id", Sort.Direction.Ascending), UUID.fromString(productId))
              .page(0, limit + 1)
              .list();
    } else {
      UUID c = UUID.fromString(CursorCodec.decode(cursor));

      reviewEntities =
          reviewRepository
              .find(
                  "product.id = ?1 and id >= ?2",
                  Sort.by("id", Sort.Direction.Ascending),
                  UUID.fromString(productId),
                  c)
              .page(0, limit + 1)
              .list();
    }

    // Convert review entity into review dto
    List<ReviewDto> reviewDtos = reviewEntities.stream().map(ReviewMapper::toDto).toList();

    boolean hasMore = reviewDtos.size() > limit;
    String nextCursor = hasMore ? CursorCodec.encode(reviewDtos.getLast().getId()) : null;
    if (hasMore) reviewDtos = reviewDtos.subList(0, reviewDtos.size() - 1);

    // Get review media
    List<MediaDto> mediaDtos = reviewDtos.stream().map(ReviewDto::getMedia).toList();

    return new ReviewMediaDto(mediaDtos, hasMore, nextCursor);
  }

  public void createProductReview(String accountId) {}
}
