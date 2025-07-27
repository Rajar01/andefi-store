package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductsDto {
  private List<ProductDto> products;

  @JsonProperty("has_more")
  private boolean hasMore;

  @JsonProperty("next_cursor")
  private String nextCursor;
}
