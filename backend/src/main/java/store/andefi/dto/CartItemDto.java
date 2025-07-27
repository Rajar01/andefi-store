package store.andefi.dto;

import lombok.Data;

@Data
public class CartItemDto {
  private String id;

  private ProductDto product;

  private long quantity;
}
