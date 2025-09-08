package store.andefi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private UUID id;
    private UUID orderId;
    private UUID orderItemId;
    private UUID productId;
    private UUID reviewerId;
    private Instant reviewAt;
    private String content;
    private Integer rating;
}
