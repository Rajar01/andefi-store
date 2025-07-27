package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import store.andefi.entity.Role;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<Role, UUID> {}
