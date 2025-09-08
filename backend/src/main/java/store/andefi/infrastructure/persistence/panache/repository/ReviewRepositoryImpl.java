package store.andefi.infrastructure.persistence.panache.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Review;
import store.andefi.core.enums.SortBy;
import store.andefi.core.repository.ReviewRepository;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderItemPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ReviewPanacheEntity;

import java.util.*;

@ApplicationScoped
public class ReviewRepositoryImpl implements ReviewRepository, PanacheRepositoryBase<ReviewPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private Review toDomainEntity(ReviewPanacheEntity reviewPanacheEntity) {
        return Review.builder()
            .id(reviewPanacheEntity.getId())
            .productId(reviewPanacheEntity.getProduct().getId())
            .reviewerId(reviewPanacheEntity.getAccount().getId())
            .orderItemId(reviewPanacheEntity.getOrderItem().getId())
            .orderId(reviewPanacheEntity.getOrderItem().getOrder().getId())
            .reviewAt(reviewPanacheEntity.getCreatedAt())
            .content(reviewPanacheEntity.getContent())
            .rating(reviewPanacheEntity.getRating())
            .build();
    }

    // Use this function when created new record only
    private ReviewPanacheEntity toPanacheEntity(Review review) {
        ProductPanacheEntity productPanacheEntity = getEntityManager()
            .getReference(ProductPanacheEntity.class, review.getProductId());

        AccountPanacheEntity accountPanacheEntity = getEntityManager()
            .getReference(AccountPanacheEntity.class, review.getReviewerId());

        OrderItemPanacheEntity orderItemPanacheEntity = getEntityManager()
            .getReference(OrderItemPanacheEntity.class, review.getOrderItemId());

        return ReviewPanacheEntity.builder()
            .id(review.getId())
            .content(review.getContent())
            .rating(review.getRating())
            .product(productPanacheEntity)
            .account(accountPanacheEntity)
            .orderItem(orderItemPanacheEntity)
            .build();
    }

    private Sort getSortBy(String sortBy) {
        if (Objects.equals(sortBy, SortBy.RATING_ASC.toString())) {
            return Sort.by("rating", Sort.Direction.Ascending).and("id", Sort.Direction.Ascending);
        }

        if (Objects.equals(sortBy, SortBy.RATING_DESC.toString())) {
            return Sort.by("rating", Sort.Direction.Descending).and("id", Sort.Direction.Ascending);
        }

        return Sort.by("id", Sort.Direction.Ascending);
    }

    @Override
    public Float calculateProductAverageRatingByProductId(UUID productId) {
        Double average = getEntityManager()
            .createQuery(
                "SELECT AVG(r.rating) FROM ReviewPanacheEntity r WHERE r.product.id = :productId",
                Double.class
            )
            .setParameter("productId", productId)
            .getSingleResult();

        // Return 0.0 if no reviews
        return average != null ? average.floatValue() : 0f;
    }

    @Override
    public Integer countReviewsWithContentByProductId(UUID productId) {
        return (int) count("product.id = ?1 and content is not null", productId);
    }

    @Override
    public Integer countRatingsByProductId(UUID productId) {
        return (int) count("product.id = ?1", productId);
    }

    @Override
    public Review findLatestReviewByProductId(UUID productId) {
        Optional<ReviewPanacheEntity> latestReviewPanacheEntity = find("product.id = ?1",
            Sort.by("createdAt", Sort.Direction.Descending),
            productId
        ).firstResultOptional();

        return latestReviewPanacheEntity.map(this::toDomainEntity).orElse(null);
    }

    @Override
    public List<Review> findProductReviews(String productId, int rating, String sortBy, int limit, String cursor) {
        // Build base query and parameters
        StringBuilder query = new StringBuilder("product.id = ?1");
        List<Object> params = new ArrayList<>();
        params.add(UUID.fromString(productId));

        // Add cursor filter if present
        if (cursor != null && !cursor.isBlank()) {
            // Get last product id and last rating
            String[] parts = cursor.split(",");

            if (sortBy.equals(SortBy.RATING_ASC.toString())) {
                query.append(" and (rating > ?").append(params.size() + 1)
                    .append(" or (rating = ?").append(params.size() + 1)
                    .append(" and id >= ?").append(params.size() + 2).append("))");
                params.add(Integer.parseInt(parts[1]));
                params.add(UUID.fromString(parts[0]));
            } else if (sortBy.equals(SortBy.RATING_DESC.toString())) {
                query.append(" and (rating < ?").append(params.size() + 1)
                    .append(" or (rating = ?").append(params.size() + 1)
                    .append(" and id >= ?").append(params.size() + 2).append("))");
                params.add(Integer.parseInt(parts[1]));
                params.add(UUID.fromString(parts[0]));
            } else {
                query.append(" and id >= ?").append(params.size() + 1);
                params.add(UUID.fromString(parts[0]));
            }
        }

        // Add rating filter if present
        if (rating > 0) {
            query.append(" and rating = ?").append(params.size() + 1);
            params.add(rating);
        }

        List<ReviewPanacheEntity> reviewPanacheEntities = find(query.toString(), getSortBy(sortBy), params.toArray())
            .page(0, limit + 1)
            .list();

        return reviewPanacheEntities.stream().map(this::toDomainEntity).toList();
    }

    @Override
    public void save(Review review) {
        // Update existing product review
        if (review.getId() != null) {
            Optional<ReviewPanacheEntity> existingReviewPanacheEntityOptional = findByIdOptional(review.getId());

            existingReviewPanacheEntityOptional.ifPresent(it -> {
                it.setRating(review.getRating());
                it.setContent(review.getContent());
                persist(it);
            });
        }

        // Create new product review
        if (review.getId() == null) {
            review.setId(UuidCreator.getTimeOrderedEpoch());
            persist(toPanacheEntity(review));
        }
    }

    @Override
    public boolean isReviewExistsByAccountIdAndOrderItemId(UUID accountId, UUID orderItemId) {
        return count("account.id = ?1 and orderItem.id = ?2", accountId, orderItemId) > 0;
    }
}
