package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Set;
import lombok.Data;

@Data
public class AccountDto {
  private String id;
  private String email;

  @JsonProperty("full_name")
  private String fullName;

  private String username;

  @JsonProperty("phone_number")
  private String phoneNumber;

  private ShippingAddressDto shippingAddress;

  private Set<String> roles;

  @JsonProperty("is_verified")
  private boolean isVerified;

  @JsonProperty("verified_at")
  private Instant verifiedAt;
}
