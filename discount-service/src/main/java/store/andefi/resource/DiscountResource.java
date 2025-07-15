package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.DiscountDto;
import store.andefi.service.DiscountService;

import java.util.UUID;

@Path("/api")
public class DiscountResource {
    @Inject
    DiscountService discountService;

    @GET
    @Path("/products/{product_id}/discount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductDiscount(@PathParam("product_id") UUID productId) {
        DiscountDto response = discountService.getProductDiscount(productId);
        return Response.ok(response).build();
    }
}
