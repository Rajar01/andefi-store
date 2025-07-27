package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemUpdateQuantityDto {
  @JsonProperty("product_id")
  private String productId;

  private long quantity;
}
