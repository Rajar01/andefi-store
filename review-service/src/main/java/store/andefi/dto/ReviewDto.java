package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class ReviewDto {
  String id;

  @JsonProperty("product_id")
  String productId;

  @NotBlank String content;

  float rating;

  AccountDto account;

  MediaDto media;

  @NotNull
  @JsonProperty("created_at")
  Instant createdAt;

  public ReviewDto() {}

  public ReviewDto(
      String id,
      String productId,
      String content,
      float rating,
      AccountDto account,
      MediaDto media,
      Instant createdAt) {
    this.id = id;
    this.productId = productId;
    this.content = content;
    this.rating = rating;
    this.account = account;
    this.media = media;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public AccountDto getAccount() {
    return account;
  }

  public void setAccount(AccountDto account) {
    this.account = account;
  }

  public MediaDto getMedia() {
    return media;
  }

  public void setMedia(MediaDto media) {
    this.media = media;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
