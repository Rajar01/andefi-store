package store.andefi.resource;

import io.quarkus.security.Authenticated;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.dto.CartDto;
import store.andefi.dto.CartItemUpdateQuantityDto;
import store.andefi.service.CartService;

@Path("/api/carts")
@RequestScoped
public class CartResource {
  @Inject CartService cartService;

  @Inject
  @Claim(standard = Claims.sub)
  String accountId;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Authenticated
  public Response getOrCreateCart() {
    CartDto cartDto = cartService.getOrCreateCart(accountId);

    return Response.ok(cartDto).build();
  }

  @POST
  @Path("/items")
  @Transactional
  @Authenticated
  public Response addProductIntoCart(JsonObject jsonObject) {
    cartService.addProductIntoCart(accountId, jsonObject.getString("product_id"));

    return Response.ok().build();
  }

  @DELETE
  @Path("/items")
  @Transactional
  @Authenticated
  public Response removeProductFromCart(JsonObject jsonObject) {
    cartService.removeProductFromCart(accountId, jsonObject.getString("product_id"));

    return Response.ok().build();
  }

  @PUT
  @Path("/items")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Authenticated
  public Response updateCartItemQuantityInCart(
      CartItemUpdateQuantityDto cartItemUpdateQuantityDto) {
    cartService.updateCartItemQuantityInCart(accountId, cartItemUpdateQuantityDto);

    return Response.ok().build();
  }
}
