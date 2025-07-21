package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import store.andefi.dto.CartDto;
import store.andefi.dto.CartItemDto;
import store.andefi.service.CartService;

@Path("/api/accounts/{account_id}/cart")
public class CartResource {
  @Inject CartService cartService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrCreateCart(@PathParam("account_id") String accountId) {
    CartDto response = cartService.getOrCreateCart(accountId);

    return Response.ok(response).build();
  }

  @POST
  @Path("/items")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addProductsIntoCart(
      @PathParam("account_id") String accountId, CartItemDto cartItemDto) {
    cartService.addProductsIntoCart(accountId, cartItemDto);

    return Response.ok().build();
  }

  @PATCH
  @Path("/items")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateProductQuantityInCart(
      @PathParam("account_id") String accountId,
      CartItemDto cartItemDto) {
    cartService.updateProductQuantityInCart(accountId, cartItemDto);

    return Response.ok().build();
  }

  @DELETE
  @Path("/items/{product_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response removeProductFromCart(
      @PathParam("account_id") String accountId, @PathParam("product_id") String productId) {
    cartService.removeProductsFromCart(accountId, List.of(productId));

    return Response.ok().build();
  }
}
