package store.andefi.service;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import store.andefi.dto.PaymentNotificationDto;
import store.andefi.dto.StockUpdateDto;
import store.andefi.entity.*;
import store.andefi.exception.InsufficientStockException;
import store.andefi.repository.PaymentRepository;

@ApplicationScoped
public class PaymentService {
  @Inject PaymentRepository paymentRepository;

  @Inject StockService stockService;

  @ConfigProperty(name = "midtrans.server-key")
  String midtransServerKey;

  @ConfigProperty(name = "midtrans.is-production")
  boolean midtransIsProduction;

  public String createTransaction(String orderId) throws MidtransError {
    Order orderEntity =
        paymentRepository.getEntityManager().getReference(Order.class, UUID.fromString(orderId));

    // Credit card
    Map<String, String> creditCard = new HashMap<>();
    creditCard.put("secure", "true");

    // Item details
    AtomicLong totalAmountAfterDiscount = new AtomicLong();
    List<Map<String, String>> itemDetails =
        orderEntity.getOrderItems().stream()
            .map(
                it -> {
                  // Price after discount
                  long totalAmountBeforeDiscount = it.getProductPrice() * it.getQuantity();
                  long totalDiscount =
                      it.getProductDiscountPercentage()
                          * it.getProductPrice()
                          * it.getQuantity()
                          / 100;
                  totalAmountAfterDiscount.set(totalAmountBeforeDiscount - totalDiscount);

                  return Map.of(
                      "name",
                      it.getProduct().getName().substring(0, 50),
                      "price",
                      String.valueOf(totalAmountAfterDiscount.get() / it.getQuantity()),
                      "quantity",
                      String.valueOf(it.getQuantity()),
                      "id",
                      it.getProduct().getId().toString());
                })
            .toList();

    // Transaction details
    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", orderEntity.getId().toString());
    transactionDetails.put("gross_amount", String.valueOf(totalAmountAfterDiscount.get()));

    // Customer details
    Account accountEntity = orderEntity.getAccount();
    Map<String, Object> customerDetails = new HashMap<>();
    customerDetails.put("email", accountEntity.getEmail());
    customerDetails.put(
        "shipping_address", Map.of("address", accountEntity.getShippingAddress().getAddress()));

    Map<String, Object> params = new HashMap<>();
    params.put("transaction_details", transactionDetails);
    params.put("credit_card", creditCard);
    params.put("item_details", itemDetails);
    params.put("customer_details", customerDetails);

    Midtrans.serverKey = midtransServerKey;
    Midtrans.isProduction = midtransIsProduction;

    return SnapApi.createTransactionToken(params);
  }

  @Transactional
  public Response notificationHandler(PaymentNotificationDto paymentNotificationDto) {
    // TODO check signature key to make sure the notification was actually sent by payment gateway

    Order orderEntity =
        paymentRepository
            .getEntityManager()
            .getReference(Order.class, UUID.fromString(paymentNotificationDto.getOrderId()));

    // Check if notification already processed to avoid processing it as duplicate entries
    String paymentStatus = paymentNotificationDto.getTransactionStatus();

    if (orderEntity.getPayment() != null
        && Objects.equals(
            orderEntity.getPayment().getTransactionStatus(),
            paymentNotificationDto.getTransactionStatus())) {
      return Response.ok().build();
    }

    Payment paymentEntity = orderEntity.getPayment();

    // Process payment notification
    String fraudStatus = paymentNotificationDto.getFraudStatus();

    if ((Objects.equals(paymentStatus, PaymentStatus.SETTLEMENT.toString())
            || Objects.equals(paymentStatus, PaymentStatus.CAPTURE.toString()))
        && Objects.equals(fraudStatus, FraudStatus.ACCEPT.toString())) {
      paymentEntity.setMethod(paymentNotificationDto.getPaymentType());
      paymentEntity.setCurrency(Currency.getInstance(paymentNotificationDto.getCurrency()));
      paymentEntity.setAmount((long) (Double.parseDouble(paymentNotificationDto.getGrossAmount())));
      paymentEntity.setTransactionId(paymentNotificationDto.getTransactionId());

      paymentEntity.setTransactionTime(paymentNotificationDto.getTransactionTime());
      paymentEntity.setTransactionStatus(paymentNotificationDto.getTransactionStatus());

      orderEntity.setStatus(OrderStatus.PAID.toString());
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime localDateTime =
          LocalDateTime.parse(paymentNotificationDto.getTransactionTime(), formatter);
      orderEntity.setPaidAt(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
      paymentRepository.persist(paymentEntity);

      // Update product stock
      orderEntity
          .getOrderItems()
          .forEach(
              it -> {
                Stock stockEntity = it.getProduct().getStock();

                // Throw error if stock is insufficient
                if (stockEntity.getAvailableQuantity() < it.getQuantity())
                  throw new InsufficientStockException();

                // Update product stock
                StockUpdateDto stockUpdateDto = new StockUpdateDto();
                stockUpdateDto.setId(stockEntity.getId().toString());
                stockUpdateDto.setProductId(stockEntity.getProduct().getId().toString());
                stockUpdateDto.setQuantityOnHand(
                    stockEntity.getQuantityOnHand() - it.getQuantity());
                stockUpdateDto.setReservedQuantity(
                    stockEntity.getReservedQuantity() - it.getQuantity());
                stockUpdateDto.setSoldQuantity(stockEntity.getSoldQuantity() + it.getQuantity());
                stockService.updateProductStock(stockUpdateDto);
              });
    } else {
      paymentEntity.setTransactionStatus(paymentStatus);
      paymentRepository.persist(paymentEntity);
    }

    return Response.ok().build();
  }
}
