package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StockDto {
  private String id;

  @JsonProperty("product_id")
  private String productId;

  @JsonProperty("quantity_on_hand")
  private long quantityOnHand;

  @JsonProperty("reserved_quantity")
  private long reservedQuantity;

  @JsonProperty("available_quantity")
  private long availableQuantity;

  @JsonProperty("sold_quantity")
  private long soldQuantity;
}
