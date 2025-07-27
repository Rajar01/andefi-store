package store.andefi.utility.mapper;

import store.andefi.dto.CartDto;
import store.andefi.entity.Cart;

public final class CartMapper {
  public static CartDto toDto(Cart cartEntity) {
    CartDto cartDto = new CartDto();
    cartDto.setId(cartEntity.getId().toString());
    cartDto.setAccount(AccountMapper.toDto(cartEntity.getAccount()));
    cartDto.setCartItems(
        cartEntity.getCartItems() != null
            ? cartEntity.getCartItems().stream().map(CartItemMapper::toDto).toList()
            : null);

    return cartDto;
  }
}
