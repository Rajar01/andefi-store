package store.andefi.core.repository;

import store.andefi.core.entity.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository {
    Float calculateProductAverageRatingByProductId(UUID productId);

    Integer countReviewsWithContentByProductId(UUID productId);

    Integer countRatingsByProductId(UUID productId);

    Review findLatestReviewByProductId(UUID productId);

    List<Review> findProductReviews(String productId, int rating, String sortBy, int limit, String cursor);

    void save(Review review);

    boolean isReviewExistsByAccountIdAndOrderItemId(UUID accountId, UUID orderItemId);
}
