package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.Stock;
import store.andefi.core.exception.StockException;
import store.andefi.core.repository.StockRepository;
import store.andefi.core.service.StockService;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class StockServiceImpl implements StockService {
    @Inject
    StockRepository stockRepository;

    @Override
    public Optional<Stock> getStockByProductIdOptional(UUID productId) {
        return stockRepository.findStockByProductIdOptional(productId);
    }

    @Override
    @Transactional
    public void updateStockByProductId(UUID productId, Stock stock) throws StockException {
        stockRepository.save(productId, stock);
    }
}
