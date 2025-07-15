package store.andefi.repository;


import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Product;

import java.util.UUID;

@ApplicationScoped
public class ProductRepository implements PanacheMongoRepositoryBase<Product, UUID> {
}
