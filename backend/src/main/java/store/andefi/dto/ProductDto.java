package store.andefi.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDto {
  private String id;
  private String name;
  private String description;
  private long price;
  private Map<String, String> attributes;
  private List<CategoryDto> categories;
  private MediaDto media;
  private DiscountDto discount;
  private StockDto stock;
}
