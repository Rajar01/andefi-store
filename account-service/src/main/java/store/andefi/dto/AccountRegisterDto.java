package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AccountRegisterDto(
    String email,
    String username,
    String password,
    @JsonProperty("role_names") List<String> roleNames) {}
