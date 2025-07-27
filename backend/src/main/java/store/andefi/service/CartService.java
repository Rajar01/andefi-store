package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import store.andefi.dto.*;
import store.andefi.entity.Account;
import store.andefi.entity.Cart;
import store.andefi.entity.CartItem;
import store.andefi.entity.Product;
import store.andefi.exception.InsufficientStockException;
import store.andefi.repository.CartRepository;
import store.andefi.utility.mapper.CartMapper;

@ApplicationScoped
public class CartService {
  @Inject CartRepository cartRepository;

  @Inject ProductService productService;
  @Inject StockService stockService;

  public CartDto getOrCreateCart(String accountId) {
    return cartRepository
        .find("account.id", UUID.fromString(accountId))
        .firstResultOptional()
        .map(CartMapper::toDto)
        .orElseGet(
            () -> {
              Account accountEntity =
                  cartRepository
                      .getEntityManager()
                      .getReference(Account.class, UUID.fromString(accountId));

              Cart cartEntity = new Cart();
              cartEntity.setAccount(accountEntity);
              cartEntity.setCartItems(new ArrayList<>());
              cartRepository.persist(cartEntity);

              return CartMapper.toDto(cartEntity);
            });
  }

  public void addProductIntoCart(String accountId, String productId) {
    CartDto cartDto = getOrCreateCart(accountId);
    Cart cartEntity =
        cartRepository
            .getEntityManager()
            .getReference(Cart.class, UUID.fromString(cartDto.getId()));

    ProductDto productDto = productService.getProductById(productId);

    StockDto stockDto = stockService.getProductStock(productDto.getId());

    // Throw error if stock is insufficient
    if (stockDto.getAvailableQuantity() < 1) throw new InsufficientStockException();

    // Increment product quantity if product already exists in cart else create new cart item
    cartEntity.getCartItems().stream()
        .filter(it -> it.getProduct().getId().toString().equals(productDto.getId()))
        .findFirst()
        .ifPresentOrElse(
            it -> it.setQuantity(it.getQuantity() + 1),
            () -> {
              Product productEntity =
                  cartRepository
                      .getEntityManager()
                      .getReference(Product.class, UUID.fromString(productDto.getId()));

              CartItem cartItemEntity = new CartItem();
              cartItemEntity.setCart(cartEntity);
              cartItemEntity.setProduct(productEntity);
              cartItemEntity.setQuantity(1);

              cartEntity.getCartItems().add(cartItemEntity);
            });

    cartRepository.persist(cartEntity);

    // Update product stock
    StockUpdateDto stockUpdateDto = new StockUpdateDto();
    stockUpdateDto.setId(stockDto.getId());
    stockUpdateDto.setProductId(stockDto.getProductId());
    stockUpdateDto.setQuantityOnHand(stockDto.getQuantityOnHand());
    stockUpdateDto.setReservedQuantity(stockDto.getReservedQuantity() + 1);
    stockUpdateDto.setSoldQuantity(stockDto.getSoldQuantity());
    stockService.updateProductStock(stockUpdateDto);
  }

  public void removeProductFromCart(String accountId, String productId) {
    CartDto cartDto = getOrCreateCart(accountId);
    Cart cartEntity =
        cartRepository
            .getEntityManager()
            .getReference(Cart.class, UUID.fromString(cartDto.getId()));

    cartEntity.getCartItems().stream()
        .filter(it -> it.getProduct().getId().toString().equals(productId))
        .findFirst()
        .ifPresentOrElse(
            it -> {
              cartEntity.getCartItems().remove(it);

              cartRepository.persist(cartEntity);

              ProductDto productDto = productService.getProductById(productId);
              StockDto stockDto = stockService.getProductStock(productDto.getId());

              // Update product stock
              StockUpdateDto stockUpdateDto = new StockUpdateDto();
              stockUpdateDto.setId(stockDto.getId());
              stockUpdateDto.setProductId(stockDto.getProductId());
              stockUpdateDto.setQuantityOnHand(stockDto.getQuantityOnHand());
              stockUpdateDto.setReservedQuantity(stockDto.getReservedQuantity() - it.getQuantity());
              stockUpdateDto.setSoldQuantity(stockDto.getSoldQuantity());
              stockService.updateProductStock(stockUpdateDto);
            },
            () -> {
              throw new NotFoundException();
            });
  }

  public void updateCartItemQuantityInCart(
      String accountId, CartItemUpdateQuantityDto cartItemUpdateQuantityDto) {
    CartDto cartDto = getOrCreateCart(accountId);
    Cart cartEntity =
        cartRepository
            .getEntityManager()
            .getReference(Cart.class, UUID.fromString(cartDto.getId()));

    ProductDto productDto = productService.getProductById(cartItemUpdateQuantityDto.getProductId());
    StockDto stockDto = stockService.getProductStock(productDto.getId());

    // Throw error if stock is insufficient
    if (stockDto.getAvailableQuantity() < cartItemUpdateQuantityDto.getQuantity())
      throw new InsufficientStockException();

    // Throw error if quantity less than or equal to 0
    if (cartItemUpdateQuantityDto.getQuantity() <= 0) throw new BadRequestException();

    cartEntity.getCartItems().stream()
        .filter(it -> it.getProduct().getId().toString().equals(productDto.getId()))
        .findFirst()
        .ifPresentOrElse(
            it -> {
              long cartItemQuantityBeforeUpdated = it.getQuantity();

              // Update cart item quantity
              it.setQuantity(cartItemUpdateQuantityDto.getQuantity());
              cartRepository.persist(cartEntity);

              // Update product stock
              StockUpdateDto stockUpdateDto = new StockUpdateDto();
              stockUpdateDto.setId(stockDto.getId());
              stockUpdateDto.setProductId(stockDto.getProductId());
              stockUpdateDto.setQuantityOnHand(stockDto.getQuantityOnHand());
              stockUpdateDto.setReservedQuantity(
                  stockDto.getReservedQuantity()
                      + (cartItemUpdateQuantityDto.getQuantity() - cartItemQuantityBeforeUpdated));
              stockUpdateDto.setSoldQuantity(stockDto.getSoldQuantity());
              stockService.updateProductStock(stockUpdateDto);
            },
            () -> {
              throw new NotFoundException();
            });
  }
}
