package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateAccountShippingAddressRequest {
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_postal_code")
    private String shippingPostalCode;
}
