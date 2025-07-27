package store.andefi.utility.mapper;

import store.andefi.dto.CartItemDto;
import store.andefi.entity.CartItem;

public final class CartItemMapper {
  public static CartItemDto toDto(CartItem cartItemEntity) {
    CartItemDto cartItemDto = new CartItemDto();
    cartItemDto.setId(cartItemEntity.getId().toString());
    cartItemDto.setProduct(ProductMapper.toDto(cartItemEntity.getProduct()));
    cartItemDto.setQuantity(cartItemEntity.getQuantity());

    return cartItemDto;
  }
}
