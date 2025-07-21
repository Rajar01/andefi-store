package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingAddressDto(
    String id, @JsonProperty("account_id") String accountId, String address) {}
