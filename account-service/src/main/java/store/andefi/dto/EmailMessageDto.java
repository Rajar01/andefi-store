package store.andefi.dto;

import java.util.List;

public record EmailMessageDto(
        String from,
        List<String> to,
        String subject,
        String body
) {
}
