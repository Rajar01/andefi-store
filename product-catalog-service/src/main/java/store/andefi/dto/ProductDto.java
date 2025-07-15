package store.andefi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        List<String> categories,
        Map<String, String> attributes
) {
}
