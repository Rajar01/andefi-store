package store.andefi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// TODO: Rename the class
@Getter
@Builder
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String email;
}
