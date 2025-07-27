package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;

@Data
public class ReviewDto {
  private String id;

  private String content;
  private float rating;

  private AccountDto account;

  @JsonProperty("product_id")
  private String productId;

  private MediaDto media;

  @JsonProperty("created_at")
  private Instant createdAt;
}
