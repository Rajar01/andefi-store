package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.ShippingAddressDto;
import store.andefi.service.ShippingAddressService;

@Path("/api")
public class ShippingAddressResource {
  @Inject ShippingAddressService shippingAddressService;

  @GET
  @Path("/accounts/{account_id}/shipping-address")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAccountShippingAddress(@PathParam("account_id") String accountId) {
    ShippingAddressDto response = shippingAddressService.getAccountShippingAddress(accountId);

    return Response.ok(response).build();
  }
}
