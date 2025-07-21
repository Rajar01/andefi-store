package store.andefi.utility.mapper;

import org.bson.types.ObjectId;
import store.andefi.dto.CartDto;
import store.andefi.entity.Cart;

public final class CartMapper {
  public static CartDto toDto(Cart cart) {
    return new CartDto(
        cart.id().toString(), cart.cartItems().stream().map(CartItemMapper::toDto).toList());
  }
}
