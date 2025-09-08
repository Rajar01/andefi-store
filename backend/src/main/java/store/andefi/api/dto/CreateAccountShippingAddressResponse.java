package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateAccountShippingAddressResponse {
    private UUID id;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_postal_code")
    private String shippingPostalCode;
}
