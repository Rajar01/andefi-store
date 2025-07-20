package store.andefi.entity;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "stocks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id"})})
public class Stock {
    @Id
    @GeneratedValue
    UUID id;

    @NotNull
    @Column(name = "product_id")
    String productId;

    @Column(name = "quantity_on_hand")
    Long quantityOnHand;

    @Column(name = "reserved_quantity")
    Long reservedQuantity;

    @Column(name = "available_quantity")
    Long availableQuantity;

    @NotNull
    @Column(name = "created_at")
    Instant createdAt;

    @NotNull
    @Column(name = "updated_at")
    Instant updatedAt;

    public Stock() {
    }

    public Stock(UUID id, String productId, Long quantityOnHand, Long reservedQuantity, Long availableQuantity, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Long quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Long getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Long reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
