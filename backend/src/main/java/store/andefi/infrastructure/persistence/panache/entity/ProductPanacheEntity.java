package store.andefi.infrastructure.persistence.panache.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"stock", "categories"})
public class ProductPanacheEntity {
    // ========================================================================================== //
    // ======================================== Identity ======================================== //
    // ========================================================================================== //
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    // ========================================================================================== //
    // ===================================== Business fields ==================================== //
    // ========================================================================================== //
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "BIGINT")
    private Long price;

    @Column(name = "discount_percentage", columnDefinition = "INTEGER")
    @ColumnDefault("0")
    private Integer discountPercentage = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB")
    private Map<String, String> attributes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "media_urls")
    private Map<String, String> mediaUrls;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1024)
    private float[] embedding;

    // ========================================================================================== //
    // ====================================== Relationships ===================================== //
    // ========================================================================================== //
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock_id")
    private StockPanacheEntity stock;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false))
    private Set<CategoryPanacheEntity> categories;

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
        if (this.embedding == null || this.embedding.length == 0) this.embedding = new float[1024];
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
