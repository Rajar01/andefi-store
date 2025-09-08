package store.andefi.api.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.api.dto.*;
import store.andefi.core.entity.Cart;
import store.andefi.core.exception.CartException;
import store.andefi.core.service.CartService;

import java.util.List;
import java.util.UUID;

@Path("/api/carts")
@RequestScoped
public class CartResource {
    @Inject
    CartService cartService;

    @Inject
    @Claim(standard = Claims.sub)
    String accountId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getOrCreateCart() {
        try {
            Cart cart = cartService.getOrCreateCartForAccount(UUID.fromString(accountId));

            List<CartItemResponse> cartItemResponses = null;

            if (cart.getCartItems() != null) {
                cartItemResponses = cart.getCartItems().stream().map(it -> CartItemResponse.builder()
                    .id(it.getId())
                    .quantity(it.getQuantity())
                    .productId(it.getProductId())
                    .build()).toList();
            }

            GetOrCreateCartResponse getOrCreateCartResponse = GetOrCreateCartResponse.builder()
                .id(cart.getId())
                .cartItems(cartItemResponses)
                .build();

            return Response.ok(getOrCreateCartResponse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get or create cart"))
                .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cart-items")
    @Authenticated
    public Response addProductIntoCart(AddProductIntoCartRequest request) {
        try {
            cartService.addProductIntoCart(UUID.fromString(accountId), request.getProductId());

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to add product into cart"))
                .build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cart-items")
    @Authenticated
    public Response removeProductFromCart(RemoveProductFromCartRequest request) {
        try {
            cartService.removeProductFromCart(UUID.fromString(accountId), request.getProductId());

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to remove product from cart"))
                .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cart-items")
    @Authenticated
    public Response updateCartItemQuantityInCart(UpdateCartItemQuantityInCartRequest request) {
        try {
            cartService.updateCartItemQuantityInCart(UUID.fromString(accountId), request.getProductId(), request.getQuantity());

            return Response.ok().build();
        } catch (CartException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("UPDATE_CART_ITEM_QUANTITY_IN_CART_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to update cart item quantity in cart"))
                .build();
        }
    }
}
