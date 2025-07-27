package store.andefi.utility.mapper;

import store.andefi.dto.OrderDto;
import store.andefi.entity.Order;

public final class OrderMapper {
  public static OrderDto toDto(Order orderEntity) {
    OrderDto orderDto = new OrderDto();
    orderDto.setId(orderEntity.getId().toString());
    orderDto.setAccountId(orderEntity.getAccount().getId().toString());
    orderDto.setOrderItems(
        orderEntity.getOrderItems() != null
            ? orderEntity.getOrderItems().stream().map(OrderItemMapper::toDto).toList()
            : null);
    orderDto.setShippingAddress(ShippingAddressMapper.toDto(orderEntity.getShippingAddress()));
    orderDto.setPayment(PaymentMapper.toDto(orderEntity.getPayment()));
    orderDto.setStatus(orderEntity.getStatus());
    orderDto.setPaidAt(orderEntity.getPaidAt());
    orderDto.setShippingAt(orderEntity.getShippingAt());
    orderDto.setCompletedAt(orderEntity.getCompletedAt());
    orderDto.setCreatedAt(orderEntity.getCreatedAt());

    return orderDto;
  }
}
