package store.andefi.core.repository;

import store.andefi.core.entity.Stock;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository {
    Optional<Stock> findStockByProductIdOptional(UUID productId);

    void save(UUID productId, Stock stock);
}
