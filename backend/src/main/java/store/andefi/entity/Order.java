package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems;

  @ManyToOne
  @JoinColumn(name = "shipping_address_id")
  private ShippingAddress shippingAddress;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  private String status = OrderStatus.UNPAID.toString();

  @Column(name = "paid_at")
  private Instant paidAt;

  @Column(name = "shipping_at")
  private Instant shippingAt;

  @Column(name = "completed_at")
  private Instant completedAt;

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
