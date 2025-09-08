package store.andefi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private UUID id;
    private Integer quantityOnHand;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer soldQuantity;
}
