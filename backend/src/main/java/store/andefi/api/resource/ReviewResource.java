package store.andefi.api.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.api.dto.CreateProductReviewRequest;
import store.andefi.api.dto.ErrorResponse;
import store.andefi.api.dto.ProductReviewCatalogCursorPageResponse;
import store.andefi.api.dto.ProductReviewCatalogItemResponse;
import store.andefi.api.pagination.CursorCodec;
import store.andefi.core.entity.Review;
import store.andefi.core.enums.SortBy;
import store.andefi.core.exception.ProductReviewCatalogException;
import store.andefi.core.exception.ProductReviewCreationException;
import store.andefi.core.model.ProductReviewCatalogCursorPage;
import store.andefi.core.service.ReviewService;

import java.util.List;
import java.util.UUID;

@Path("/api/reviews")
@RequestScoped
public class ReviewResource {
    @Inject
    ReviewService reviewService;

    @Inject
    @Claim(standard = Claims.sub)
    String accountId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductReviewCatalog(
        @QueryParam("product_id") String productId,
        @QueryParam("rating") int rating,
        @QueryParam("sort_by") @DefaultValue(SortBy.DEFAULT_SORT_BY) String sortBy,
        @QueryParam("limit") @DefaultValue("10") int limit,
        @QueryParam("cursor") String cursor) {
        try {
            String decodedCursor = cursor == null || cursor.isBlank() ? "" : CursorCodec.decode(cursor);
            ProductReviewCatalogCursorPage cursorPage = reviewService.getProductReviewCatalogByProductId(productId, rating, sortBy, limit, decodedCursor);

            List<ProductReviewCatalogItemResponse> productReviewCatalogItemResponses = cursorPage.getReviews()
                .stream().map(it -> ProductReviewCatalogItemResponse.builder()
                    .reviewerName(it.getReviewerName())
                    .reviewAt(it.getReviewAt())
                    .content(it.getContent())
                    .rating(it.getRating())
                    .build()
                ).toList();

            String encodedCursor = cursorPage.isHasMore() ? CursorCodec.encode(cursorPage.getCursor()) : "";
            ProductReviewCatalogCursorPageResponse cursorPageResponse = ProductReviewCatalogCursorPageResponse.builder()
                .reviews(productReviewCatalogItemResponses)
                .cursor(encodedCursor)
                .hasMore(cursorPage.isHasMore())
                .build();

            return Response.ok(cursorPageResponse).build();
        } catch (ProductReviewCatalogException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PRODUCT_REVIEW_CATALOG_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get product review catalog"))
                .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response createProductReview(@Valid CreateProductReviewRequest request) {
        try {
            Review review = Review.builder()
                .reviewerId(UUID.fromString(accountId))
                .orderId(request.getOrderId())
                .orderItemId(request.getOrderItemId())
                .productId(request.getProductId())
                .content(request.getContent())
                .rating(request.getRating())
                .build();

            reviewService.createProductReview(review);

            return Response.status(Response.Status.CREATED).build();
        } catch (ProductReviewCreationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("CREATE_PRODUCT_REVIEW_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get product review catalog"))
                .build();
        }
    }
}
