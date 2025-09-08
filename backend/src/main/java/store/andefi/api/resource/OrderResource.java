package store.andefi.api.resource;

import com.midtrans.httpclient.error.MidtransError;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.api.dto.*;
import store.andefi.core.entity.Order;
import store.andefi.core.entity.OrderItem;
import store.andefi.core.entity.Product;
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.exception.*;
import store.andefi.core.model.OrderCatalog;
import store.andefi.core.service.OrderService;
import store.andefi.core.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/api/orders")
@RequestScoped
public class OrderResource {
    @Inject
    OrderService orderService;

    @Inject
    ReviewService reviewService;

    @Inject
    @Claim(standard = Claims.sub)
    String accountId;

    @POST
    @Path("/checkout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response checkout(CheckoutOrderRequest request) {
        try {
            List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(it -> OrderItem.builder()
                    .product(Product.builder().id(it.getProductId()).build())
                    .quantity(it.getQuantity())
                    .build()
                ).toList();

            Order order = orderService.checkout(UUID.fromString(accountId), orderItems);

            List<CheckoutOrderItemResponse> checkoutOrderItemsResponse = order.getOrderItems().stream().map(it -> CheckoutOrderItemResponse.builder()
                .productName(it.getProduct().getName())
                .productPrice(it.getProduct().getPrice())
                .productDiscountPercentage(it.getProduct().getDiscountPercentage())
                .mediaUrl(it.getProduct().getMediaUrls().get("primary_image"))
                .quantity(it.getQuantity())
                .build()
            ).toList();

            CheckoutOrderResponse checkoutOrderResponse = CheckoutOrderResponse.builder()
                .orderItems(checkoutOrderItemsResponse)
                .shippingAddress(Optional.ofNullable(order.getShippingAddress()).map(ShippingAddress::getAddress).orElse(null))
                .shippingPostalCode(Optional.ofNullable(order.getShippingAddress()).map(ShippingAddress::getPostalCode).orElse(null))
                .subTotal(order.getSubTotal())
                .grandTotal(order.getGrandTotal())
                .totalDiscount(order.getTotalDiscount())
                .build();

            return Response.ok(checkoutOrderResponse).build();
        } catch (CheckoutOrderException | ProductStockCheckException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ORDER_CHECKOUT_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to checkout the order"))
                .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response createOrder(CreateOrderRequest request) {
        try {
            List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(it -> OrderItem.builder()
                    .product(Product.builder().id(it.getProductId()).build())
                    .quantity(it.getQuantity())
                    .build()
                ).toList();

            Order order = orderService.createOrder(UUID.fromString(accountId), orderItems);

            CreateOrderResponse createOrderResponse = CreateOrderResponse.builder()
                .orderId(order.getId())
                .paymentUrl(order.getPaymentUrl())
                .build();

            return Response.status(Response.Status.CREATED).entity(createOrderResponse).build();
        } catch (OrderCreationException | GeneratePaymentUrlException | MidtransError e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("CREATE_ORDER_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to create the order"))
                .build();
        }
    }

    @POST
    @Path("/{order_id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response cancelOrder(@PathParam("order_id") UUID orderId) {
        try {
            Order order = orderService.getOrderByIdOptional(orderId).orElseThrow(() -> {
                    String message = String.format("Order with id %s not found", orderId.toString());
                    return new OrderCancellationException(message);
                }
            );

            orderService.cancelOrder(order);

            return Response.ok().build();
        } catch (OrderCancellationException | StockException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("CANCEL_ORDER_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to cancel the order"))
                .build();
        }
    }

    @GET
    @Path("/{order_id}/payment-url")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getOrderPaymentUrl(@PathParam("order_id") UUID orderId) {
        try {
            Order order = orderService.getOrderByIdOptional(orderId).orElse(new Order());

            GetOrderPaymentUrlResponse getOrderPaymentUrlResponse = GetOrderPaymentUrlResponse.builder()
                .orderId(orderId)
                .paymentUrl(order.getPaymentUrl())
                .build();

            return Response.ok(getOrderPaymentUrlResponse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get order payment url"))
                .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getAccountOrderCatalog(
        @QueryParam("order_status") @DefaultValue("") String orderStatus,
        @QueryParam("period") @DefaultValue("") String period
    ) {
        try {
            OrderCatalog orderCatalog = orderService.getOrderCatalogByAccountId(UUID.fromString(accountId), orderStatus, period);

            // Convert order summary domain model into order summary response
            List<OrderSummaryResponse> orderSummaryResponses = orderCatalog.getOrderSummaries().stream().map(orderSummary -> {
                List<OrderItemSummaryResponse> orderItemSummaryResponses = orderSummary.getOrderItemSummaries().stream().map(orderItemSummary -> OrderItemSummaryResponse.builder()
                    .id(orderItemSummary.getId())
                    .productName(orderItemSummary.getProductName())
                    .productPrice(orderItemSummary.getProductPrice())
                    .productDiscountPercentage(orderItemSummary.getProductDiscountPercentage())
                    .productMediaUrl(orderItemSummary.getProductMediaUrl())
                    .quantity(orderItemSummary.getQuantity())
                    .build()
                ).toList();

                return OrderSummaryResponse.builder()
                    .id(orderSummary.getId())
                    .orderItemSummaries(orderItemSummaryResponses)
                    .orderStatus(orderSummary.getOrderStatus())
                    .grandTotal(orderSummary.getGrandTotal())
                    .paymentUrl(orderSummary.getPaymentUrl())
                    .purchaseAt(orderSummary.getPurchaseAt())
                    .build();
            }).toList();

            OrderCatalogResponse orderCatalogResponse = OrderCatalogResponse.builder()
                .accountId(UUID.fromString(accountId))
                .orderSummaries(orderSummaryResponses)
                .build();

            return Response.ok(orderCatalogResponse).build();
        } catch (OrderCatalogException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ORDER_CATALOG_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to get order catalog"))
                .build();
        }
    }

    @GET
    @Path("/{order_id}")
    @Authenticated
    public Response getOrderDetails(@PathParam("order_id") UUID orderId) {
        try {
            Order order = orderService.getOrderByIdOptional(orderId).orElse(null);

            if (order == null) {
                String message = String.format("Order with id %s not found", orderId);
                throw new OrderDetailsException(message);
            }

            List<OrderItemDetailsResponse> orderItemDetailsResponses = order.getOrderItems().stream().map(it -> OrderItemDetailsResponse.builder()
                .id(it.getId())
                .productId(it.getProduct().getId())
                .productName(it.getProduct().getName())
                .productPrice(it.getProduct().getPrice())
                .productDiscountPercentage(it.getProduct().getDiscountPercentage())
                .productMediaUrl(it.getProduct().getMediaUrls().get("primary_image"))
                .quantity(it.getQuantity())
                .isReviewed(reviewService.isOrderItemReviewedByAccount(UUID.fromString(accountId), it.getId()))
                .build()
            ).toList();

            OrderDetailsResponse orderDetailsResponse = OrderDetailsResponse.builder()
                .id(order.getId())
                .accountId(order.getAccountId())
                .orderItems(orderItemDetailsResponses)
                .shippingAddress(order.getShippingAddress().getAddress())
                .shippingPostalCode(order.getShippingAddress().getPostalCode())
                .orderStatus(order.getOrderStatus())
                .transactionStatus(order.getTransactionStatus())
                .subTotal(order.getSubTotal())
                .grandTotal(order.getGrandTotal())
                .totalDiscount(order.getTotalDiscount())
                .paymentMethod(order.getPaymentMethod())
                .paymentUrl(order.getPaymentUrl())
                .purchaseAt(order.getPurchaseAt())
                .paidAt(order.getPaidAt())
                .shippingAt(order.getShippingAt())
                .completedAt(order.getCompletedAt())
                .build();

            return Response.ok(orderDetailsResponse).build();
        } catch (OrderDetailsException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ORDER_DETAILS_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            String message = String.format("Failed to get order with id %s", orderId);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", message))
                .build();
        }
    }
}
