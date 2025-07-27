package store.andefi.utility.mapper;

import store.andefi.dto.OrderItemDto;
import store.andefi.entity.OrderItem;

public final class OrderItemMapper {
  public static OrderItemDto toDto(OrderItem orderItemEntity) {
    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setId(orderItemEntity.getId().toString());
    orderItemDto.setProductId(orderItemEntity.getProduct().getId().toString());
    orderItemDto.setProductName(orderItemEntity.getProductName());
    orderItemDto.setProductPrice(orderItemEntity.getProductPrice());
    orderItemDto.setProductDiscountPercentage(orderItemEntity.getProductDiscountPercentage());
    orderItemDto.setProductMediaUrls(orderItemEntity.getProductMediaUrls());
    orderItemDto.setQuantity(orderItemEntity.getQuantity());

    return orderItemDto;
  }
}
