package store.andefi.resource;

import com.midtrans.httpclient.error.MidtransError;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.dto.OrderCheckoutDto;
import store.andefi.dto.OrderCheckoutResponseDto;
import store.andefi.dto.OrderDto;
import store.andefi.service.OrderService;

@Path("/api/orders")
@RequestScoped
public class OrderResource {
  @Inject OrderService orderService;

  @Inject
  @Claim(standard = Claims.sub)
  String accountId;

  @POST
  @Path("/checkout")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Authenticated
  public Response checkoutOrder(OrderCheckoutDto orderCheckoutDto) throws MidtransError {
    OrderCheckoutResponseDto orderCheckoutResponseDto =
        orderService.checkout(accountId, orderCheckoutDto);

    return Response.ok(orderCheckoutResponseDto).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authenticated
  public Response getOrders(
      @QueryParam("order_status") @DefaultValue("") String orderStatus,
      @QueryParam("period") @DefaultValue("") String period) {
    List<OrderDto> orderDtos = orderService.getOrders(accountId, orderStatus, period);

    return Response.ok(orderDtos).build();
  }

  @GET
  @Path("/{order_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authenticated
  public Response getOrder(@PathParam("order_id") String orderId) {
    OrderDto orderDto = orderService.getOrder(orderId);

    return Response.ok(orderDto).build();
  }
}
