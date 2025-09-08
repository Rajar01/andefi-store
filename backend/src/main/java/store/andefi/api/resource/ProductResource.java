package store.andefi.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.api.dto.ErrorResponse;
import store.andefi.api.dto.LatestReviewResponse;
import store.andefi.api.dto.ProductCatalogCursorPageResponse;
import store.andefi.api.dto.ProductDetailsResponse;
import store.andefi.api.pagination.CursorCodec;
import store.andefi.core.exception.EmbeddingException;
import store.andefi.core.exception.ProductCatalogException;
import store.andefi.core.exception.ProductDetailsException;
import store.andefi.core.exception.ProductSearchException;
import store.andefi.core.model.ProductCatalogCursorPage;
import store.andefi.core.model.ProductDetails;
import store.andefi.core.service.ProductService;

import java.util.Optional;

@Path("/api/products")
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductCatalog(
        @QueryParam("category") @DefaultValue("") String category,
        @QueryParam("limit") @DefaultValue("10") int limit,
        @QueryParam("cursor") @DefaultValue("") String cursor
    ) {
        try {
            String decodedCursor = cursor.isBlank() ? "" : CursorCodec.decode(cursor);
            ProductCatalogCursorPage cursorPage = productService.getProductCatalog(category, limit, decodedCursor);

            String encodedCursor = cursorPage.isHasMore() ? CursorCodec.encode(cursorPage.getCursor()) : "";
            ProductCatalogCursorPageResponse cursorPageResponse = ProductCatalogCursorPageResponse.builder()
                .productSummaries(cursorPage.getProductSummaries())
                .cursor(encodedCursor)
                .hasMore(cursorPage.isHasMore())
                .build();

            return Response.ok(cursorPageResponse).build();
        } catch (ProductCatalogException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PRODUCT_CATALOG_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get product catalog"))
                .build();
        }
    }

    @GET
    @Path("/{product_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductDetails(@PathParam("product_id") String productId) {
        try {
            ProductDetails productDetails = productService.getProductDetailsByProductId(productId);

            LatestReviewResponse latestReviewResponse = Optional.ofNullable(productDetails.getLatestReview())
                .map(it -> LatestReviewResponse.builder()
                    .reviewerName(it.getReviewerName())
                    .reviewAt(it.getReviewAt())
                    .content(it.getContent())
                    .rating(it.getRating())
                    .build())
                .orElse(null);

            ProductDetailsResponse productDetailsResponse = ProductDetailsResponse.builder()
                .id(productDetails.getId())
                .name(productDetails.getName())
                .description(productDetails.getDescription())
                .price(productDetails.getPrice())
                .discountPercentage(productDetails.getDiscountPercentage())
                .attributes(productDetails.getAttributes())
                .mediaUrls(productDetails.getMediaUrls())
                .soldQuantity(productDetails.getSoldQuantity())
                .totalReviews(productDetails.getTotalReviews())
                .totalRatings(productDetails.getTotalRatings())
                .averageRating(productDetails.getAverageRating())
                .latestReview(latestReviewResponse)
                .build();

            return Response.ok(productDetailsResponse).build();
        } catch (ProductDetailsException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PRODUCT_DETAILS_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            String message = String.format("Failed to get product with id %s", productId);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", message))
                .build();
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProduct(
        @QueryParam("keyword") @DefaultValue("") String keyword,
        @QueryParam("limit") @DefaultValue("10") int limit,
        @QueryParam("cursor") String cursor
    ) {
        try {
            String decodedCursor = cursor == null || cursor.isBlank() ? "" : CursorCodec.decode(cursor);
            ProductCatalogCursorPage cursorPage = productService.semanticSearchProducts(keyword, limit, decodedCursor);

            String encodedCursor = cursorPage.isHasMore() ? CursorCodec.encode(cursorPage.getCursor()) : "";
            ProductCatalogCursorPageResponse cursorPageResponse = ProductCatalogCursorPageResponse.builder()
                .productSummaries(cursorPage.getProductSummaries())
                .cursor(encodedCursor)
                .hasMore(cursorPage.isHasMore())
                .build();

            return Response.ok(cursorPageResponse).build();
        } catch (ProductSearchException | EmbeddingException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PRODUCT_SEARCH_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Product search failed"))
                .build();
        }
    }
}
