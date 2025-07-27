package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "discounts")
@Data
public class Discount {
  @Id @GeneratedValue private UUID id;

  @Column(name = "discount_percentage")
  private long discountPercentage;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "start_date")
  private Instant startDate;

  @Column(name = "end_date")
  private Instant endDate;

  @OneToMany(mappedBy = "discount")
  private List<Product> products;

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
