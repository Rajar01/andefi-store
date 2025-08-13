package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "products")
@Data
public class Product {
  @Id private UUID id;
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;
  private long price;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, String> attributes;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "media_id")
  private Media media;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "discount_id")
  private Discount discount;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @ManyToMany
  @JoinTable(
      name = "product_category",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private List<Category> categories;

  @Column
  @JdbcTypeCode(SqlTypes.VECTOR)
  @Array(length = 1024)
  private float[] embedding;

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
