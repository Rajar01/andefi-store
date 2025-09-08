package store.andefi.core.service;

import store.andefi.core.entity.Stock;
import store.andefi.core.exception.StockException;

import java.util.Optional;
import java.util.UUID;

public interface StockService {
    Optional<Stock> getStockByProductIdOptional(UUID productId);

    void updateStockByProductId(UUID productId, Stock stock) throws StockException;
}
