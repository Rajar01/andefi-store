package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
  @Id @GeneratedValue private UUID id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  private List<Account> accounts;

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
