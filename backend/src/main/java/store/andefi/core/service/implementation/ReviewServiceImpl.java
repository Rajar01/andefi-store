package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.Account;
import store.andefi.core.entity.Product;
import store.andefi.core.entity.Review;
import store.andefi.core.exception.ProductReviewCatalogException;
import store.andefi.core.exception.ProductReviewCreationException;
import store.andefi.core.model.LatestReview;
import store.andefi.core.model.ProductReviewCatalogCursorPage;
import store.andefi.core.model.ProductReviewCatalogItem;
import store.andefi.core.repository.ReviewRepository;
import store.andefi.core.service.AccountService;
import store.andefi.core.service.OrderService;
import store.andefi.core.service.ProductService;
import store.andefi.core.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ReviewServiceImpl implements ReviewService {
    @Inject
    AccountService accountService;

    @Inject
    ProductService productService;

    @Inject
    OrderService orderService;

    @Inject
    ReviewRepository reviewRepository;

    @Override
    public Float getProductAverageRatingByProductId(UUID productId) {
        return reviewRepository.calculateProductAverageRatingByProductId(productId);
    }

    @Override
    public Integer getProductTotalReviewsByProductId(UUID productId) {
        return reviewRepository.countReviewsWithContentByProductId(productId);
    }

    @Override
    public Integer getProductTotalRatingsByProductId(UUID productId) {
        return reviewRepository.countRatingsByProductId(productId);
    }

    @Override
    public LatestReview getProductLatestReviewByProductId(UUID productId) {
        Review review = reviewRepository.findLatestReviewByProductId(productId);

        if (review == null) return null;

        String reviewerName = accountService.findAccountByIdOptional(review.getReviewerId())
            .map(Account::getFullName)
            .orElse(null);

        return LatestReview.builder()
            .reviewerName(reviewerName)
            .reviewAt(review.getReviewAt())
            .content(review.getContent())
            .rating(review.getRating())
            .build();
    }

    @Override
    public ProductReviewCatalogCursorPage getProductReviewCatalogByProductId(String productId, int rating, String sortBy, int limit, String cursor) throws ProductReviewCatalogException {
        if (productId == null || productId.isBlank()) {
            throw new ProductReviewCatalogException("Product id can not be blank or null");
        }

        if (limit < 1 || limit > 100) {
            throw new ProductReviewCatalogException("Limit must be between 1 and 100");
        }

        List<Review> reviews = reviewRepository.findProductReviews(productId, rating, sortBy, limit, cursor);

        boolean hasMore = reviews.size() > limit;
        String nextCursor = hasMore ? String.format("%s,%s", reviews.getLast().getId().toString(), reviews.getLast().getRating().toString()) : null;
        List<ProductReviewCatalogItem> productReviewCatalogItems = reviews.stream().map(it -> {
                String reviewerName = accountService.findAccountByIdOptional(it.getReviewerId())
                    .map(Account::getFullName)
                    .orElse(null);

                return ProductReviewCatalogItem.builder()
                    .reviewerName(reviewerName)
                    .reviewAt(it.getReviewAt())
                    .content(it.getContent())
                    .rating(it.getRating())
                    .build();
            }
        ).toList();

        if (hasMore)
            productReviewCatalogItems = productReviewCatalogItems.subList(0, productReviewCatalogItems.size() - 1);

        return ProductReviewCatalogCursorPage.builder()
            .reviews(productReviewCatalogItems)
            .cursor(nextCursor)
            .hasMore(hasMore)
            .build();
    }

    @Override
    @Transactional
    public void createProductReview(Review review) throws ProductReviewCreationException {
        if (reviewRepository.isReviewExistsByAccountIdAndOrderItemId(review.getReviewerId(), review.getOrderItemId())) {
            String message = String.format("Already reviewed the order item with id %s", review.getOrderItemId());
            throw new ProductReviewCreationException(message);
        }

        // Rating validation
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new ProductReviewCreationException("Rating must be between 1 and 5");
        }

        Optional<Account> accountOptional = accountService.findAccountByIdOptional(review.getReviewerId());

        if (accountOptional.isEmpty()) {
            throw new ProductReviewCreationException("Account not found");
        }

        if (!orderService.isOrderItemBelongsToOrder(review.getOrderId(), review.getOrderItemId())) {
            String message = String.format("Order item with id %s not belong to order with id %s", review.getOrderItemId(), review.getOrderId());
            throw new ProductReviewCreationException(message);
        }

        Optional<Product> productOptional = productService.getProductByIdOptional(review.getProductId());

        if (productOptional.isEmpty()) {
            String message = String.format("Product with id %s not found", review.getProductId());
            throw new ProductReviewCreationException(message);
        }

        reviewRepository.save(review);
    }

    @Override
    public boolean isOrderItemReviewedByAccount(UUID accountId, UUID orderItemId) {
        return reviewRepository.isReviewExistsByAccountIdAndOrderItemId(accountId, orderItemId);
    }
}
