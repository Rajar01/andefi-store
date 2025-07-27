package store.andefi.service;

import com.midtrans.httpclient.error.MidtransError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import store.andefi.dto.*;
import store.andefi.entity.*;
import store.andefi.exception.InsufficientStockException;
import store.andefi.repository.OrderRepository;
import store.andefi.utility.Period;
import store.andefi.utility.mapper.OrderMapper;

@ApplicationScoped
public class OrderService {
  @Inject OrderRepository orderRepository;

  @Inject StockService stockService;
  @Inject PaymentService paymentService;

  public OrderCheckoutResponseDto checkout(String accountId, OrderCheckoutDto orderCheckoutDto)
      throws MidtransError {
    Account accountEntity =
        orderRepository.getEntityManager().getReference(Account.class, UUID.fromString(accountId));

    Order orderEntity = new Order();
    OrderCheckoutResponseDto orderCheckoutResponseDto = new OrderCheckoutResponseDto();

    List<OrderItem> orderItemEntities =
        orderCheckoutDto.getOrderItem().stream()
            .map(
                it -> {
                  Product productEntity =
                      orderRepository
                          .getEntityManager()
                          .getReference(Product.class, UUID.fromString(it.getProductId()));

                  StockDto stockDto =
                      stockService.getProductStock(productEntity.getId().toString());

                  // Throw error if stock is insufficient
                  if (stockDto.getAvailableQuantity() < it.getQuantity())
                    throw new InsufficientStockException();

                  // Update product stock
                  StockUpdateDto stockUpdateDto = new StockUpdateDto();
                  stockUpdateDto.setId(stockDto.getId());
                  stockUpdateDto.setProductId(stockDto.getProductId());
                  stockUpdateDto.setQuantityOnHand(stockDto.getQuantityOnHand());
                  stockUpdateDto.setReservedQuantity(
                      stockDto.getReservedQuantity() + it.getQuantity());
                  stockUpdateDto.setSoldQuantity(stockDto.getSoldQuantity());
                  stockService.updateProductStock(stockUpdateDto);

                  OrderItem orderItemEntity = new OrderItem();
                  orderItemEntity.setOrder(orderEntity);
                  orderItemEntity.setProduct(productEntity);
                  orderItemEntity.setProductName(productEntity.getName());
                  orderItemEntity.setProductPrice(productEntity.getPrice());

                  // Check if discount is valid or not, if not return 1
                  Instant now = Instant.now();
                  boolean isProductDiscountValid =
                      !now.isBefore(productEntity.getDiscount().getStartDate())
                          && !now.isAfter(productEntity.getDiscount().getEndDate());
                  orderItemEntity.setProductDiscountPercentage(
                      isProductDiscountValid
                          ? productEntity.getDiscount().getDiscountPercentage()
                          : 1);

                  orderItemEntity.setProductMediaUrls(productEntity.getMedia().getUrls());
                  orderItemEntity.setQuantity(it.getQuantity());

                  long totalAmountBeforeDiscount =
                      orderItemEntity.getProductPrice() * orderItemEntity.getQuantity();
                  long totalDiscount =
                      orderItemEntity.getProductDiscountPercentage()
                          * orderItemEntity.getProductPrice()
                          * orderItemEntity.getQuantity()
                          / 100;
                  long totalAmountAfterDiscount = totalAmountBeforeDiscount - totalDiscount;

                  orderCheckoutResponseDto.setTotalAmountBeforeDiscount(
                      orderCheckoutResponseDto.getTotalAmountBeforeDiscount()
                          + totalAmountBeforeDiscount);
                  orderCheckoutResponseDto.setTotalDiscount(
                      orderCheckoutResponseDto.getTotalDiscount() + totalDiscount);
                  orderCheckoutResponseDto.setTotalAmountAfterDiscount(
                      orderCheckoutResponseDto.getTotalAmountAfterDiscount()
                          + totalAmountAfterDiscount);

                  return orderItemEntity;
                })
            .toList();

    Payment paymentEntity = new Payment();
    paymentEntity.setOrder(orderEntity);
    paymentEntity.setAccount(accountEntity);

    orderEntity.setAccount(accountEntity);
    orderEntity.setOrderItems(orderItemEntities);
    orderEntity.setShippingAddress(accountEntity.getShippingAddress());
    orderEntity.setPayment(paymentEntity);
    orderRepository.persist(orderEntity);

    String transactionToken = paymentService.createTransaction(orderEntity.getId().toString());
    orderCheckoutResponseDto.setTransactionToken(transactionToken);
    orderCheckoutResponseDto.setOrderId(orderEntity.getId().toString());

    return orderCheckoutResponseDto;
  }

  @Transactional
  public List<OrderDto> getOrders(String accountId, String orderStatus, String period) {
    StringBuilder query = new StringBuilder("account.id = ?1");
    List<Object> params = new ArrayList<>(List.of(UUID.fromString(accountId)));
    int paramsIndex = 1;

    if (orderStatus != null && !orderStatus.isBlank()) {
      query.append(String.format(" and status = ?%d", ++paramsIndex));
      params.add(orderStatus);
    }

    if (period.equals(Period.LAST_30_DAYS.toString())) {
      Instant now = Instant.now();

      query.append(
          String.format(
              " and (createdAt >= ?%d and createdAt <= ?%d)", ++paramsIndex, ++paramsIndex));
      params.add(now.minus(Duration.ofDays(30)));
      params.add(now);
    } else if (period.equals(Period.LAST_60_DAYS.toString())) {
      Instant now = Instant.now();

      query.append(
          String.format(
              " and (createdAt >= ?%d and createdAt <= ?%d)", ++paramsIndex, ++paramsIndex));
      params.add(now.minus(Duration.ofDays(60)));
      params.add(now);
    } else if (period.equals(Period.LAST_90_DAYS.toString())) {
      Instant now = Instant.now();

      query.append(
          String.format(
              " and (createdAt >= ?%d and createdAt <= ?%d)", ++paramsIndex, ++paramsIndex));
      params.add(now.minus(Duration.ofDays(90)));
      params.add(now);
    }

    return orderRepository.find(query.toString(), params.toArray()).stream()
        .map(OrderMapper::toDto)
        .toList();
  }

  public OrderDto getOrder(String orderId) {
    return orderRepository
        .findByIdOptional(UUID.fromString(orderId))
        .map(OrderMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }
}
