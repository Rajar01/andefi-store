package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.ReviewsDto;
import store.andefi.service.ReviewService;

@Path("/api")
public class ReviewResource {
  @Inject ReviewService reviewService;

  @GET
  @Path("/products/{product_id}/reviews")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductReviews(
      @PathParam("product_id") String productId,
      @QueryParam("limit") @DefaultValue("10") int limit,
      @QueryParam("cursor") String cursor) {
    ReviewsDto response = reviewService.getProductReviews(productId, limit, cursor);

    return Response.ok(response).build();
  }
}
