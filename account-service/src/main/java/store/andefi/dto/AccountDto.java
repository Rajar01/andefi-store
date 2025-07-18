package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public class AccountDto {
  String id;

  String email;

  String username;

  List<RoleDto> roles;

  boolean isVerified;

  @JsonProperty("verified_at")
  Instant verifiedAt;

  public AccountDto() {}

  public AccountDto(
      String id,
      String email,
      String username,
      List<RoleDto> roles,
      boolean isVerified,
      Instant verifiedAt) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.roles = roles;
    this.isVerified = isVerified;
    this.verifiedAt = verifiedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDto> roles) {
    this.roles = roles;
  }

  @JsonProperty("is_verified")
  public boolean isVerified() {
    return isVerified;
  }

  public void setVerified(boolean verified) {
    isVerified = verified;
  }

  public Instant getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(Instant verifiedAt) {
    this.verifiedAt = verifiedAt;
  }
}
