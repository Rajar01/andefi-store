package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Stock;

import java.util.UUID;

@ApplicationScoped
public class StockRepository implements PanacheRepositoryBase<Stock, UUID> {
}
