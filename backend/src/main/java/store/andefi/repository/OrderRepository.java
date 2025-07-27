package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import store.andefi.entity.Order;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<Order, UUID> {
}
