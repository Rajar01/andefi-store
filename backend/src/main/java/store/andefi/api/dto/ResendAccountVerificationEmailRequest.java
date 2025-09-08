package store.andefi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResendAccountVerificationEmailRequest {
    private String email;
}
