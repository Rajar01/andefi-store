package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.Cart;
import store.andefi.core.exception.CartException;
import store.andefi.core.repository.CartRepository;
import store.andefi.core.service.CartService;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CartServiceImpl implements CartService {
    @Inject
    CartRepository cartRepository;

    @Override
    @Transactional
    public Cart getOrCreateCartForAccount(UUID accountId) {
        Optional<Cart> cartOptional = cartRepository.findCartByAccountIdOptional(accountId);

        // Return existing cart if present
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        }

        Cart cart = Cart.builder().accountId(accountId).build();

        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addProductIntoCart(UUID accountId, UUID productId) {
        Cart cart = cartRepository.findCartByAccountIdOptional(accountId).orElseGet(() -> getOrCreateCartForAccount(accountId));

        cartRepository.addCartItemIntoCart(cart, productId);
    }

    @Override
    @Transactional
    public void removeProductFromCart(UUID accountId, UUID productId) {
        Cart cart = cartRepository.findCartByAccountIdOptional(accountId).orElse(null);

        if (cart == null) return;

        cartRepository.removeCartItemFromCart(cart, productId);
    }

    @Override
    @Transactional
    public void updateCartItemQuantityInCart(UUID accountId, UUID productId, Integer quantity) throws CartException {
        if (quantity == null || quantity <= 0)
            throw new CartException("Quantity cannot be blank and must be greater than 0");

        Cart cart = cartRepository.findCartByAccountIdOptional(accountId).orElse(null);

        if (cart == null) throw new CartException("Cart associated with account not found");

        cartRepository.updateCartItemQuantityInCart(cart, productId, quantity);
    }
}
