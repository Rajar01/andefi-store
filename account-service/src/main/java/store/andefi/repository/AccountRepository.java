package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Account;

@ApplicationScoped
public class AccountRepository implements PanacheMongoRepository<Account> {}
