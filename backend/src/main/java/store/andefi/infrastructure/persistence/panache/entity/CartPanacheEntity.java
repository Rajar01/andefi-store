package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"account", "cartItems"})
public class CartPanacheEntity {
    // ========================================================================================== //
    // ======================================== Identity ======================================== //
    // ========================================================================================== //
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountPanacheEntity account;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemPanacheEntity> cartItems;

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
        if (this.cartItems == null || this.cartItems.isEmpty()) this.cartItems = List.of();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
