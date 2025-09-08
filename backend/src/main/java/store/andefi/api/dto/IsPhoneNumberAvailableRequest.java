package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IsPhoneNumberAvailableRequest {
    @JsonProperty("phone_number")
    private String phoneNumber;
}
