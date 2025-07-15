package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Discount;

import java.util.UUID;

@ApplicationScoped
public class DiscountRepository implements PanacheMongoRepositoryBase<Discount, UUID> {
}
