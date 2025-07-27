package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.ReviewMediaDto;
import store.andefi.dto.ReviewsDto;
import store.andefi.dto.ReviewsSummaryDto;
import store.andefi.service.ReviewService;
import store.andefi.utility.SortBy;

@Path("/api/reviews")
public class ReviewResource {
  @Inject ReviewService reviewService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductReviews(
      @QueryParam("product_id") String productId,
      @QueryParam("rating") int rating,
      @QueryParam("sort_by") @DefaultValue(SortBy.DEFAULT_SORT_BY) String sortBy,
      @QueryParam("limit") @DefaultValue("10") int limit,
      @QueryParam("cursor") String cursor) {
    ReviewsDto reviewsDto =
        reviewService.getProductReviews(productId, rating, sortBy, limit, cursor);

    return Response.ok(reviewsDto).build();
  }

  @GET
  @Path("/summary")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductReviewSummary(@QueryParam("product_id") String productId) {
    ReviewsSummaryDto reviewsSummaryDto = reviewService.getProductReviewSummary(productId);

    return Response.ok(reviewsSummaryDto).build();
  }

  @GET
  @Path("/media")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductReviewMedia(
      @QueryParam("product_id") String productId,
      @QueryParam("limit") @DefaultValue("10") int limit,
      @QueryParam("cursor") String cursor) {
    ReviewMediaDto reviewMediaDto = reviewService.getProductReviewMedia(productId, limit, cursor);

    return Response.ok(reviewMediaDto).build();
  }
}
