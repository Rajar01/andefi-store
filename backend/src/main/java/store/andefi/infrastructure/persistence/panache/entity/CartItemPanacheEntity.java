package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cart", "product"})
public class CartItemPanacheEntity {
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
    @Column(nullable = false)
    private Integer quantity;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartPanacheEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
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
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
