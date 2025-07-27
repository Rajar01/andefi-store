package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Data;

@Data
public class DiscountDto {
  private String id;

  @JsonProperty("discount_percentage")
  private long discountPercentage;

  @JsonProperty("is_active")
  private boolean isActive;

  @JsonProperty("start_date")
  private Instant startDate;

  @JsonProperty("end_date")
  private Instant endDate;
}
