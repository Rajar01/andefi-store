package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Cart;
import store.andefi.core.entity.CartItem;
import store.andefi.core.repository.CartRepository;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.CartItemPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.CartPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CartRepositoryImpl implements CartRepository, PanacheRepositoryBase<CartPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private Cart toDomainEntity(CartPanacheEntity cartPanacheEntity) {
        Set<CartItem> cartItems = null;

        if (cartPanacheEntity.getCartItems() != null) {
            cartItems = cartPanacheEntity.getCartItems().stream().map(it -> CartItem.builder()
                .id(it.getId())
                .quantity(it.getQuantity())
                .productId(it.getProduct().getId())
                .build()
            ).collect(Collectors.toSet());
        }

        return Cart.builder()
            .id(cartPanacheEntity.getId())
            .accountId(cartPanacheEntity.getAccount().getId())
            .cartItems(cartItems)
            .build();
    }

    // Use this function when created new record only
    private CartPanacheEntity toPanacheEntity(Cart cart) {
        AccountPanacheEntity accountPanacheEntity = getEntityManager()
            .getReference(AccountPanacheEntity.class, cart.getAccountId());

        return CartPanacheEntity.builder()
            .id(cart.getId())
            .account(accountPanacheEntity)
            .build();
    }

    @Override
    public Optional<Cart> findCartByAccountIdOptional(UUID accountId) {
        return find("account.id", accountId).firstResultOptional().map(this::toDomainEntity);
    }

    @Override
    public Cart save(Cart cart) {
        persist(toPanacheEntity(cart));
        Optional<Cart> cartOptional = findCartByAccountIdOptional(cart.getAccountId());
        return cartOptional.orElse(null);
    }

    @Override
    public void addCartItemIntoCart(Cart cart, UUID productId) {
        CartPanacheEntity cartPanacheEntity = findById(cart.getId());

        CartItemPanacheEntity existingCartItemPanacheEntity = cartPanacheEntity.getCartItems()
            .stream().filter(it -> productId.equals(it.getProduct().getId())).findFirst().orElse(null);

        // If product already exist in cart increment the cart item quantity, otherwise add cart item into cart.
        if (existingCartItemPanacheEntity != null) {
            existingCartItemPanacheEntity.setQuantity(existingCartItemPanacheEntity.getQuantity() + 1);
        } else {
            ProductPanacheEntity productPanacheEntity = getEntityManager()
                .getReference(ProductPanacheEntity.class, productId);

            CartItemPanacheEntity newCartItemPanacheEntity = CartItemPanacheEntity.builder()
                .quantity(1)
                .cart(cartPanacheEntity)
                .product(productPanacheEntity)
                .build();

            cartPanacheEntity.getCartItems().add(newCartItemPanacheEntity);
        }

        persist(cartPanacheEntity);
    }

    @Override
    public void updateCartItemQuantityInCart(Cart cart, UUID productId, int quantity) {
        CartPanacheEntity cartPanacheEntity = findById(cart.getId());

        cartPanacheEntity.getCartItems()
            .stream().filter(it -> productId.equals(it.getProduct().getId())).findFirst()
            .ifPresent(it -> it.setQuantity(quantity));
    }

    @Override
    public void removeCartItemFromCart(Cart cart, UUID productId) {
        CartPanacheEntity cartPanacheEntity = findById(cart.getId());

        CartItemPanacheEntity CartItemPanacheEntity = cartPanacheEntity.getCartItems()
            .stream().filter(it -> productId.equals(it.getProduct().getId())).findFirst().orElse(null);

        if (CartItemPanacheEntity == null) return;

        cartPanacheEntity.getCartItems().remove(CartItemPanacheEntity);
    }
}
