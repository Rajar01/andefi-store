package store.andefi.utility.mapper;

import store.andefi.dto.ReviewDto;
import store.andefi.entity.Review;

public final class ReviewMapper {
  public static ReviewDto toDto(Review reviewEntity) {
    ReviewDto reviewDto = new ReviewDto();
    reviewDto.setId(reviewEntity.getId().toString());
    reviewDto.setContent(reviewEntity.getContent());
    reviewDto.setRating(reviewEntity.getRating());
    reviewDto.setAccount(AccountMapper.toDto(reviewEntity.getAccount()));
    reviewDto.setProductId(reviewEntity.getProduct().getId().toString());
    reviewDto.setMedia(MediaMapper.toDto(reviewEntity.getMedia()));
    reviewDto.setCreatedAt(reviewEntity.getCreatedAt());

    return reviewDto;
  }
}
