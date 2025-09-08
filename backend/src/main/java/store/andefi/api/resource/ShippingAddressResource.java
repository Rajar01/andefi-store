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
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.exception.ShippingAddressCreationException;
import store.andefi.core.exception.ShippingAddressException;
import store.andefi.core.service.ShippingAddressService;

import java.util.UUID;

@Path("/api/shipping-addresses")
@RequestScoped
public class ShippingAddressResource {
    @Inject
    ShippingAddressService shippingAddressService;

    @Inject
    @Claim(standard = Claims.sub)
    String accountId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getAccountShippingAddress() {
        try {
            ShippingAddress shippingAddress = shippingAddressService.getShippingAddressByAccountIdOptional(UUID.fromString(accountId)).orElse(null);

            if (shippingAddress == null) {
                String message = "Account shipping address not found";

                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("GET_ACCOUNT_SHIPPING_ADDRESS_ERROR", message))
                    .build();
            }

            GetAccountShippingAddressResponse getAccountShippingAddressResponse = GetAccountShippingAddressResponse.builder()
                .id(shippingAddress.getId())
                .shippingAddress(shippingAddress.getAddress())
                .shippingPostalCode(shippingAddress.getPostalCode())
                .build();

            return Response.ok(getAccountShippingAddressResponse).build();
        } catch (ShippingAddressException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("GET_ACCOUNT_SHIPPING_ADDRESS_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get account shipping address"))
                .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response createAccountShippingAddress(CreateAccountShippingAddressRequest request) {
        try {
            if (shippingAddressService.isAccountShippingAddressExist(UUID.fromString(accountId))) {
                throw new ShippingAddressCreationException("Account shipping address already exist");
            }

            ShippingAddress shippingAddress = ShippingAddress.builder()
                .address(request.getShippingAddress())
                .postalCode(request.getShippingPostalCode()).build();

            ShippingAddress newShippingAddress = shippingAddressService.createAccountShippingAddress(UUID.fromString(accountId), shippingAddress);

            CreateAccountShippingAddressResponse createAccountShippingAddressResponse = CreateAccountShippingAddressResponse.builder()
                .id(newShippingAddress.getId())
                .shippingAddress(newShippingAddress.getAddress())
                .shippingPostalCode(newShippingAddress.getPostalCode())
                .build();

            return Response.status(Response.Status.CREATED).entity(createAccountShippingAddressResponse).build();
        } catch (ShippingAddressCreationException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("CREATE_ACCOUNT_SHIPPING_ADDRESS_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to create account shipping address"))
                .build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response updateAccountShippingAddress(UpdateAccountShippingAddressRequest request) {
        try {
            if (!shippingAddressService.isAccountShippingAddressExist(UUID.fromString(accountId))) {
                throw new ShippingAddressCreationException("Account shipping address not exist");
            }

            ShippingAddress shippingAddress = ShippingAddress.builder()
                .address(request.getShippingAddress())
                .postalCode(request.getShippingPostalCode()).build();

            ShippingAddress updatedShippingAddress = shippingAddressService.updateAccountShippingAddress(UUID.fromString(accountId), shippingAddress);

            UpdateAccountShippingAddressResponse updatedAccountShippingAddressResponse = UpdateAccountShippingAddressResponse.builder()
                .id(updatedShippingAddress.getId())
                .shippingAddress(updatedShippingAddress.getAddress())
                .shippingPostalCode(updatedShippingAddress.getPostalCode())
                .build();

            return Response.ok(updatedAccountShippingAddressResponse).build();
        } catch (ShippingAddressException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("UPDATE_ACCOUNT_SHIPPING_ADDRESS_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to update account shipping address"))
                .build();
        }
    }
}
