package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {
  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "order_id")
  private Order order;

  private String method;

  private Currency currency;

  private long amount;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "transaction_time")
  private String transactionTime;

  @Column(name = "transaction_status")
  private String transactionStatus;

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
