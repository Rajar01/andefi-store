package store.andefi.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import store.andefi.dto.DiscountDto;

@Path("/api/discounts")
@RegisterRestClient
public interface DiscountService {
    @GET
    @Path("/{discount_id}")
    @Produces(MediaType.APPLICATION_JSON)
    DiscountDto getDiscountById(@PathParam("discount_id") String discountId);
}
