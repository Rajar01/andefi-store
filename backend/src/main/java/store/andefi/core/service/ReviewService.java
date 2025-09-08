package store.andefi.core.service;

import store.andefi.core.entity.Review;
import store.andefi.core.exception.ProductReviewCatalogException;
import store.andefi.core.exception.ProductReviewCreationException;
import store.andefi.core.model.LatestReview;
import store.andefi.core.model.ProductReviewCatalogCursorPage;

import java.util.UUID;

public interface ReviewService {
    Float getProductAverageRatingByProductId(UUID productId);

    Integer getProductTotalReviewsByProductId(UUID productId);

    Integer getProductTotalRatingsByProductId(UUID productId);

    LatestReview getProductLatestReviewByProductId(UUID productId);

    ProductReviewCatalogCursorPage getProductReviewCatalogByProductId(String productId, int rating, String sortBy, int limit, String cursor) throws ProductReviewCatalogException;

    void createProductReview(Review review) throws ProductReviewCreationException;

    boolean isOrderItemReviewedByAccount(UUID accountId, UUID orderItemId);
}
