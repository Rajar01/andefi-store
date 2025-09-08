package store.andefi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IsEmailAvailableResponse {
    private String email;
    private boolean available;
}
