package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "accounts")
@Data
public class Account {
  @Id @GeneratedValue private UUID id;
  private String email;

  @Column(name = "full_name")
  private String fullName;

  private String username;
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "shipping_address_id")
  private ShippingAddress shippingAddress;

  @ManyToMany
  @JoinTable(
      name = "account_role",
      joinColumns = @JoinColumn(name = "account_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  @Column(name = "is_verified")
  private boolean isVerified;

  @Column(name = "verified_at")
  private Instant verifiedAt;

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
