package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "product")
public class StockPanacheEntity {
    // ========================================================================================== //
    // ======================================== Identity ======================================== //
    // ========================================================================================== //
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    // ========================================================================================== //
    // ===================================== Business fields ==================================== //
    // ========================================================================================== //
    @Column(name = "quantity_on_hand", columnDefinition = "INTEGER")
    @ColumnDefault("0")
    private Integer quantityOnHand = 0;

    @Column(name = "available_quantity", columnDefinition = "INTEGER")
    @ColumnDefault("0")
    private Integer availableQuantity = 0;

    @Column(name = "reserved_quantity", columnDefinition = "INTEGER")
    @ColumnDefault("0")
    private Integer reservedQuantity = 0;

    @Column(name = "sold_quantity", columnDefinition = "INTEGER")
    @ColumnDefault("0")
    private Integer soldQuantity = 0;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @OneToOne(mappedBy = "stock")
    private ProductPanacheEntity product;

    // ========================================================================================== //
    // ======================================== Metadata ======================================== //
    // ========================================================================================== //
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        if (this.quantityOnHand == null) this.quantityOnHand = 0;
        if (this.availableQuantity == null) this.availableQuantity = this.quantityOnHand;
        if (this.reservedQuantity == null) this.reservedQuantity = 0;
        if (this.soldQuantity == null) this.soldQuantity = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
