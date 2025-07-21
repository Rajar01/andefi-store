package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Cart;

@ApplicationScoped
public class CartRepository implements PanacheMongoRepository<Cart> {}
