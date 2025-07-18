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

@Path("/api/discounts")
public class DiscountResource {
    @Inject
    DiscountService discountService;

    @GET
    @Path("/{discount_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscountById(@PathParam("discount_id") String discountId) {
        DiscountDto response = discountService.getDiscountById(discountId);

        return Response.ok(response).build();
    }
}
