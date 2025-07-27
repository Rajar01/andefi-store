package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShippingAddressUpdateDto {
  private String address;

  @JsonProperty("address_other_details")
  private String addressOtherDetails;
}
