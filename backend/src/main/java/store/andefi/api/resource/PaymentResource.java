package store.andefi.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import store.andefi.api.dto.ErrorResponse;
import store.andefi.api.dto.PaymentNotificationRequest;
import store.andefi.core.entity.Order;
import store.andefi.core.exception.OrderCancellationException;
import store.andefi.core.exception.OrderRefundException;
import store.andefi.core.exception.PaymentException;
import store.andefi.core.exception.StockException;
import store.andefi.core.service.OrderService;
import store.andefi.core.service.PaymentService;

import java.util.UUID;

@Path("/api/payments")
public class PaymentResource {
    @Inject
    PaymentService paymentService;

    @Inject
    OrderService orderService;

    @ConfigProperty(name = "midtrans.server-key")
    String midtransServerKey;

    @POST
    @Path("/notification")
    public Response processPaymentNotification(PaymentNotificationRequest request) {
        try {
            // Verifying signature key to ensure request come from midtrans
            String signatureKey = DigestUtils.sha512Hex(request.getOrderId() + request.getStatusCode() + request.getGrossAmount() + midtransServerKey);
            if (!request.getSignatureKey().equals(signatureKey)) return Response.ok().build();

            Order order = orderService.getOrderByIdOptional(UUID.fromString(request.getOrderId())).orElse(null);

            // Skip processing if order is missing or already has the same transaction status as the request
            if (order == null || (order.getTransactionStatus() != null && order.getTransactionStatus().toString().equalsIgnoreCase(request.getTransactionStatus()))) {
                return Response.ok().build();
            }

            // For these transaction status do nothing: deny, failure, authorize, and pending

            if ((request.getTransactionStatus().equals("capture") && request.getFraudStatus().equals("accept")) ||
                request.getTransactionStatus().equals("settlement")) {
                orderService.processSuccessfulPayment(order, request.getPaymentType(), request.getCurrency());
            }

            if (request.getTransactionStatus().equals("expire")) {
                orderService.processExpirePayment(order);
            }

            if (request.getTransactionStatus().equals("cancel")) {
                orderService.cancelOrder(order);
            }

            if (request.getTransactionStatus().equals("refund")) {
                orderService.refundOrder(order, false);
            }

            if (request.getTransactionStatus().equals("partial_refund")) {
                orderService.refundOrder(order, true);
            }

            return Response.ok().build();
        } catch (PaymentException | StockException | OrderCancellationException | OrderRefundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("PAYMENT_NOTIFICATION_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to process payment notification"))
                .build();
        }
    }
}
