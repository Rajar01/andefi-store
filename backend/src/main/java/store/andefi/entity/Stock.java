package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "stocks")
@Data
public class Stock {
  @Id @GeneratedValue private UUID id;

  @Column(name = "quantity_on_hand")
  private long quantityOnHand;

  @Column(name = "reserved_quantity")
  private long reservedQuantity;

  @Column(name = "available_quantity")
  private long availableQuantity;

  @Column(name = "sold_quantity")
  private long soldQuantity;

  @OneToOne(mappedBy = "stock")
  private Product product;

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
