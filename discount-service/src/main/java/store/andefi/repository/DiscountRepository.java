package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Discount;

@ApplicationScoped
public class DiscountRepository implements PanacheMongoRepository<Discount> {
}
