package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"order", "product"})
public class OrderItemPanacheEntity {
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
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price", columnDefinition = "BIGINT")
    private Long productPrice;

    @Column(name = "product_discount_percentage")
    private Integer productDiscountPercentage;

    @Column(name = "product_media_url", nullable = false)
    private String productMediaUrl;

    @Column(nullable = false)
    private Integer quantity;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderPanacheEntity order;

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
