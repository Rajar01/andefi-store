package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.ShippingAddress;

@ApplicationScoped
public class ShippingAddressRepository implements PanacheMongoRepository<ShippingAddress> {}
