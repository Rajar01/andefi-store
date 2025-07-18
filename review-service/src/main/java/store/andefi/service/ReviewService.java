package store.andefi.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import store.andefi.dto.AccountDto;
import store.andefi.dto.MediaDto;
import store.andefi.dto.ReviewDto;
import store.andefi.dto.ReviewsDto;
import store.andefi.entity.Review;
import store.andefi.repository.ReviewRepository;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.mapper.ReviewMapper;

@ApplicationScoped
public class ReviewService {
  @Inject ReviewRepository reviewRepository;

  @Inject @RestClient MediaService mediaService;
  @Inject @RestClient AccountService accountService;

  public ReviewsDto getProductReviews(String productId, int limit, String cursor) {
    List<Review> reviewEntities;

    if (limit < 1 || limit > 100) {
      throw new BadRequestException();
    }

    if (cursor == null || cursor.isBlank()) {
      reviewEntities =
          reviewRepository
              .find("product_id", Sort.by("_id", Sort.Direction.Ascending), productId)
              .page(0, limit + 1)
              .list();
    } else {
      UUID c = UUID.fromString(CursorCodec.decode(cursor));

      reviewEntities =
          reviewRepository
              .find(
                  "product_id = ?1 and _id >= ?2",
                  Sort.by("_id", Sort.Direction.Ascending),
                  productId,
                  c)
              .page(0, limit + 1)
              .list();
    }

    // Convert reviews entity into reviews dto
    List<ReviewDto> reviewDtos =
        reviewEntities.stream()
            .map(
                reviewEntity -> {
                  // TODO: Handle if media and account not found
                  MediaDto mediaDto = mediaService.getMediaById(reviewEntity.mediaId());
                  AccountDto accountDto = accountService.getAccountById(reviewEntity.accountId());

                  ReviewDto reviewDto = ReviewMapper.toDto(reviewEntity);
                  reviewDto.setMedia(mediaDto);
                  reviewDto.setAccount(accountDto);
                  return reviewDto;
                })
            .toList();

    boolean hasMore = reviewDtos.size() > limit;
    String nextCursor = hasMore ? CursorCodec.encode(reviewDtos.getLast().getId()) : null;
    if (hasMore) reviewDtos = reviewDtos.subList(0, reviewDtos.size() - 1);

    return new ReviewsDto(reviewDtos, hasMore, nextCursor);
  }
}
