package store.andefi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {
  @Id @GeneratedValue private UUID id;

  private String name;

  @ManyToMany(mappedBy = "categories")
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
