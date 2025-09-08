package store.andefi.core.repository;

import store.andefi.core.entity.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findCartByAccountIdOptional(UUID accountId);

    // This function is just for create new cart
    Cart save(Cart cart);

    void addCartItemIntoCart(Cart cart, UUID productId);

    void updateCartItemQuantityInCart(Cart cart, UUID productId, int quantity);

    void removeCartItemFromCart(Cart cart, UUID productId);
}
