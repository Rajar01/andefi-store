package store.andefi.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountSignUpDto {
  private String email;
  private String username;
  private String password;
  private Set<String> roles;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @JsonProperty("full_name")
  private String fullName;
}
