package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.core.embedding.EmbeddingProvider;
import store.andefi.core.entity.Product;
import store.andefi.core.exception.*;
import store.andefi.core.model.LatestReview;
import store.andefi.core.model.ProductCatalogCursorPage;
import store.andefi.core.model.ProductDetails;
import store.andefi.core.model.ProductSummary;
import store.andefi.core.repository.ProductRepository;
import store.andefi.core.service.ProductService;
import store.andefi.core.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {
    @Inject
    ReviewService reviewService;

    @Inject
    ProductRepository productRepository;

    @Inject
    EmbeddingProvider embeddingProvider;

    @Override
    public ProductCatalogCursorPage getProductCatalog(String category, int limit, String cursor) throws ProductCatalogException {
        if (limit < 1 || limit > 100) {
            throw new ProductCatalogException("Limit must be between 1 and 100");
        }

        List<Product> products = (category.isBlank()) ? productRepository.findAllProducts(limit, cursor)
            : productRepository.findProductByCategory(category, limit, cursor);

        boolean hasMore = products.size() > limit;
        String nextCursor = hasMore ? products.getLast().getId().toString() : null;
        List<ProductSummary> productSummaries = products.stream().map(it -> {
                // TODO: Create media url type enum (primary_image, gallery_image, etc)
                String mediaUrl = it.getMediaUrls().get("primary_image");
                Float averageRating = reviewService.getProductAverageRatingByProductId(it.getId());

                return ProductSummary.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .description(it.getDescription())
                    .price(it.getPrice())
                    .discountPercentage(it.getDiscountPercentage())
                    .mediaUrl(mediaUrl)
                    .soldQuantity(it.getStock().getSoldQuantity())
                    .averageRating(averageRating)
                    .build();
            }
        ).toList();

        if (hasMore) productSummaries = productSummaries.subList(0, productSummaries.size() - 1);

        return ProductCatalogCursorPage.builder()
            .productSummaries(productSummaries)
            .cursor(nextCursor)
            .hasMore(hasMore)
            .build();
    }

    @Override
    public ProductDetails getProductDetailsByProductId(String productId) throws ProductDetailsException {
        Optional<Product> productOptional = productRepository.findProductByIdOptional(UUID.fromString(productId));

        if (productOptional.isEmpty()) {
            throw new ProductDetailsException(String.format("Product with id %s not found", productId));
        }

        Product product = productOptional.get();

        Float averageRating = reviewService.getProductAverageRatingByProductId(product.getId());
        Integer totalReviews = reviewService.getProductTotalReviewsByProductId(product.getId());
        Integer totalRatings = reviewService.getProductTotalRatingsByProductId(product.getId());
        LatestReview latestReview = reviewService.getProductLatestReviewByProductId(product.getId());

        return ProductDetails.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .discountPercentage(product.getDiscountPercentage())
            .attributes(product.getAttributes())
            .mediaUrls(product.getMediaUrls())
            .soldQuantity(product.getStock().getSoldQuantity())
            .totalReviews(totalReviews)
            .totalRatings(totalRatings)
            .averageRating(averageRating)
            .latestReview(latestReview)
            .build();
    }

    @Override
    public Optional<Product> getProductByIdOptional(UUID productId) {
        return productRepository.findProductByIdOptional(productId);
    }

    @Override
    public ProductCatalogCursorPage semanticSearchProducts(String keyword, int limit, String cursor) throws ProductSearchException, EmbeddingException {
        if (limit < 1 || limit > 100) {
            throw new ProductSearchException("Limit must be between 1 and 100");
        }

        if (keyword == null || keyword.isBlank()) {
            throw new ProductSearchException("Keyword can not be blank or null");
        }

        // Transform keyword into text embedding
        float[][] embeddingKeyword = embeddingProvider.generateEmbeddingText(keyword);

        List<Product> products = productRepository.semanticSearch(embeddingKeyword[0], limit, cursor);

        // Compute the L2 distance between the keyword embedding and the last product in the current result set.
        // This distance will be used for pagination cursor generation.
        UUID lastProductId = products.getLast().getId();
        Double lastDistance = productRepository.calculateProductEmbeddingL2Distance(embeddingKeyword[0], lastProductId);

        boolean hasMore = products.size() > limit;
        String nextCursor = hasMore ? String.format("%s,%s", lastProductId.toString(), lastDistance.toString()) : null;
        List<ProductSummary> productSummaries = products.stream().map(it -> {
                // TODO: Create media url type enum (primary_image, gallery_image, etc)
                String mediaUrl = it.getMediaUrls().get("primary_image");
                Float averageRating = reviewService.getProductAverageRatingByProductId(it.getId());

                return ProductSummary.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .description(it.getDescription())
                    .price(it.getPrice())
                    .discountPercentage(it.getDiscountPercentage())
                    .mediaUrl(mediaUrl)
                    .soldQuantity(it.getStock().getSoldQuantity())
                    .averageRating(averageRating)
                    .build();
            }
        ).toList();

        if (hasMore) productSummaries = productSummaries.subList(0, productSummaries.size() - 1);

        return ProductCatalogCursorPage.builder()
            .productSummaries(productSummaries)
            .cursor(nextCursor)
            .hasMore(hasMore)
            .build();
    }

    @Override
    public boolean isProductStockSufficient(UUID productId, Integer quantity) throws ProductStockCheckException {
        Product product = getProductByIdOptional(productId).orElse(null);

        if (product == null) {
            String message = String.format("Product with id %s not found", productId.toString());
            throw new ProductStockCheckException(message);
        }

        return product.getStock().getAvailableQuantity() > quantity;
    }
}
