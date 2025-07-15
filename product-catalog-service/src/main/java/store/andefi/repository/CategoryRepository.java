package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Category;

import java.util.UUID;

@ApplicationScoped
public class CategoryRepository implements PanacheMongoRepositoryBase<Category, UUID> {
}
