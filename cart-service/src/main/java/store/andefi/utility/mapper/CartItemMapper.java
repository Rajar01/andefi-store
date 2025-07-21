package store.andefi.utility.mapper;

import store.andefi.dto.CartItemDto;
import store.andefi.entity.CartItem;

public final class CartItemMapper {
  public static CartItemDto toDto(CartItem cartItem) {
    return new CartItemDto(cartItem.productId(), cartItem.quantity());
  }

  public static CartItem toEntity(CartItemDto cartItemDto) {
    return new CartItem(cartItemDto.productId(), cartItemDto.quantity());
  }
}
