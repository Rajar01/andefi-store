package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipping_addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "account")
public class ShippingAddressPanacheEntity {
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
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @OneToOne(mappedBy = "shippingAddress")
    private AccountPanacheEntity account;

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
