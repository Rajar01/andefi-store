package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Role;

@ApplicationScoped
public class RoleRepository implements PanacheMongoRepository<Role> {}
