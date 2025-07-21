package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import store.andefi.dto.CartDto;
import store.andefi.dto.CartItemDto;
import store.andefi.dto.StockDto;
import store.andefi.dto.StockUpdateDto;
import store.andefi.entity.Cart;
import store.andefi.entity.CartItem;
import store.andefi.exception.AccountNotFoundException;
import store.andefi.exception.DuplicationException;
import store.andefi.repository.CartRepository;
import store.andefi.utility.mapper.CartItemMapper;
import store.andefi.utility.mapper.CartMapper;

@ApplicationScoped
public class CartService {
  @Inject CartRepository cartRepository;

  @Inject @RestClient AccountService accountService;
  @Inject @RestClient StockService stockService;

  public CartDto getOrCreateCart(String accountId) {
    Cart cartEntity =
        cartRepository
            .find("account_id", accountId)
            .firstResultOptional()
            .orElseGet(
                () -> {
                  accountService
                      .getAccountById(accountId)
                      .orElseThrow(AccountNotFoundException::new);

                  Cart c =
                      new Cart(
                          new ObjectId(),
                          accountId,
                          new ArrayList<>(),
                          Instant.now(),
                          Instant.now());
                  cartRepository.persist(c);

                  return c;
                });

    return CartMapper.toDto(cartEntity);
  }

  public void addProductsIntoCart(String accountId, CartItemDto cartItemDto) {
    Cart cartEntity =
        cartRepository
            .find("account_id", accountId)
            .firstResultOptional()
            .orElseGet(
                () -> {
                  accountService
                      .getAccountById(accountId)
                      .orElseThrow(AccountNotFoundException::new);

                  Cart c =
                      new Cart(
                          new ObjectId(),
                          accountId,
                          new ArrayList<>(),
                          Instant.now(),
                          Instant.now());

                  cartRepository.persist(c);

                  return c;
                });

    CartItem cartItemEntity = CartItemMapper.toEntity(cartItemDto);

    // Return error if product already in cart
    cartEntity
        .cartItems()
        .forEach(
            c -> {
              if (c.productId().equals(cartItemEntity.productId()))
                throw new DuplicationException();
            });

    StockDto stockDto =
        stockService
            .getProductStock(cartItemEntity.productId())
            .orElseThrow(NotFoundException::new);

    //  Check if product stock available, if not return error
    if (stockDto.availableQuantity() < cartItemEntity.quantity()) throw new RuntimeException();

    // Update product stock
    stockService.updateProductStock(
        stockDto.productId(),
        new StockUpdateDto(
            stockDto.productId(),
            stockDto.quantityOnHand(),
            stockDto.reservedQuantity() + cartItemDto.quantity()));

    cartEntity.cartItems().add(cartItemEntity);

    cartRepository.update(
        new Cart(
            cartEntity.id(),
            cartEntity.accountId(),
            cartEntity.cartItems(),
            cartEntity.createdAt(),
            Instant.now()));
  }

  public void removeProductsFromCart(String accountId, List<String> productIds) {
    Cart cartEntity =
        cartRepository
            .find("account_id", accountId)
            .firstResultOptional()
            .orElseThrow(NotFoundException::new);

    // Update product stock
    for (String productId : productIds) {
      CartItemDto cartItemDto =
          cartEntity.cartItems().stream()
              .filter(cartItem -> cartItem.productId().equals(productId))
              .map(CartItemMapper::toDto)
              .findFirst()
              .orElseThrow(NotFoundException::new);

      stockService.getProductStock(productId);

      StockDto stockDto =
          stockService.getProductStock(productId).orElseThrow(NotFoundException::new);

      stockService.updateProductStock(
          stockDto.productId(),
          new StockUpdateDto(
              stockDto.productId(),
              stockDto.quantityOnHand(),
              stockDto.reservedQuantity() - cartItemDto.quantity()));
    }

    cartEntity.cartItems().removeIf(cartItem -> productIds.contains(cartItem.productId()));

    cartRepository.update(cartEntity);
  }

  public void updateProductQuantityInCart(String accountId, CartItemDto cartItemDto) {
    Cart cartEntity =
        cartRepository
            .find("account_id", accountId)
            .firstResultOptional()
            .orElseThrow(NotFoundException::new);

    CartItem cartItemEntity =
        cartEntity.cartItems().stream()
            .filter(cartItem -> cartItem.productId().equals(cartItemDto.productId()))
            .findFirst()
            .orElseThrow(NotFoundException::new);

    StockDto stockDto =
        stockService.getProductStock(cartItemDto.productId()).orElseThrow(NotFoundException::new);

    stockService.updateProductStock(
        stockDto.productId(),
        new StockUpdateDto(
            stockDto.productId(),
            stockDto.quantityOnHand(),
            stockDto.reservedQuantity() + (cartItemDto.quantity() - cartItemEntity.quantity())));

    cartEntity
        .cartItems()
        .removeIf(cartItem -> cartItem.productId().equals(cartItemDto.productId()));

    cartEntity.cartItems().add(new CartItem(cartItemDto.productId(), cartItemDto.quantity()));

    cartRepository.update(cartEntity);
  }
}
