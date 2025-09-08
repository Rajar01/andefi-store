package store.andefi.core.service;

import store.andefi.core.entity.Cart;
import store.andefi.core.exception.CartException;

import java.util.UUID;

public interface CartService {
    Cart getOrCreateCartForAccount(UUID accountId);

    void addProductIntoCart(UUID accountId, UUID productId);

    void removeProductFromCart(UUID accountId, UUID productId);

    void updateCartItemQuantityInCart(UUID accountId, UUID productId, Integer quantity) throws CartException;
}
