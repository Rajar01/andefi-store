package store.andefi.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.dto.ShippingAddressDto;
import store.andefi.dto.ShippingAddressUpdateDto;
import store.andefi.service.ShippingAddressService;

@Path("/api/shipping-addresses")
@RequestScoped
public class ShippingAddressResource {
  @Inject ShippingAddressService shippingAddressService;

  @Inject
  @Claim(standard = Claims.sub)
  String accountId;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authenticated
  public Response getAccountShippingAddress() {
    ShippingAddressDto shippingAddressDto =
        shippingAddressService.getAccountShippingAddress(accountId);

    return Response.ok(shippingAddressDto).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Authenticated
  public Response updateShippingAddress(ShippingAddressUpdateDto shippingAddressUpdateDto) {
    shippingAddressService.updateShippingAddress(accountId, shippingAddressUpdateDto);

    return Response.ok().build();
  }
}
