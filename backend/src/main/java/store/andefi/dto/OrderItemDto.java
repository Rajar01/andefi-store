package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Map;
import lombok.Data;

@Data
public class OrderItemDto {
  private String id;

  @JsonProperty("product_id")
  private String productId;

  @JsonProperty("product_name")
  private String productName;

  @JsonProperty("product_price")
  private long productPrice;

  @JsonProperty("product_discount_percentage")
  private long productDiscountPercentage;

  @JsonProperty("product_media_urls")
  private Map<String, String> productMediaUrls;

  private long quantity;
}
