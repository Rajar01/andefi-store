package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "product_price")
  private long productPrice;

  @Column(name = "product_discount_percentage")
  private long productDiscountPercentage;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "product_media_urls", columnDefinition = "jsonb")
  private Map<String, String> productMediaUrls;

  private long quantity;

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
