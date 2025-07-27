package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.PaymentNotificationDto;
import store.andefi.service.PaymentService;

@Path("/api/payments")
public class PaymentResource {
  @Inject PaymentService paymentService;

  @POST
  @Path("/notification")
  @Transactional
  public Response notificationHandler(PaymentNotificationDto paymentNotificationDto) {
    return paymentService.notificationHandler(paymentNotificationDto);
  }
}
