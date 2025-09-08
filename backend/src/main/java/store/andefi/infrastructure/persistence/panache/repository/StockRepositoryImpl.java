package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Stock;
import store.andefi.core.repository.StockRepository;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.StockPanacheEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class StockRepositoryImpl implements StockRepository, PanacheRepositoryBase<StockPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private Stock toDomainEntity(StockPanacheEntity stockPanacheEntity) {
        return Stock.builder()
            .id(stockPanacheEntity.getId())
            .quantityOnHand(stockPanacheEntity.getQuantityOnHand())
            .availableQuantity(stockPanacheEntity.getAvailableQuantity())
            .reservedQuantity(stockPanacheEntity.getReservedQuantity())
            .soldQuantity(stockPanacheEntity.getSoldQuantity())
            .build();
    }

    // Use this function when created new record only
    private StockPanacheEntity toPanacheEntity(Stock stock) {
        return StockPanacheEntity.builder()
            .id(stock.getId())
            .quantityOnHand(stock.getQuantityOnHand())
            .availableQuantity(stock.getAvailableQuantity())
            .reservedQuantity(stock.getReservedQuantity())
            .soldQuantity(stock.getSoldQuantity())
            .build();
    }

    @Override
    public Optional<Stock> findStockByProductIdOptional(UUID productId) {
        return find("product.id", productId).firstResultOptional().map(this::toDomainEntity);
    }

    @Override
    public void save(UUID productId, Stock stock) {
        StockPanacheEntity stockPanacheEntity = getEntityManager()
            .createQuery("FROM StockPanacheEntity p WHERE p.product.id = :product_id", StockPanacheEntity.class)
            .setParameter("product_id", productId)
            .getSingleResultOrNull();

        // Update existing stock
        if (stockPanacheEntity != null) {
            stockPanacheEntity.setQuantityOnHand(stock.getQuantityOnHand());
            stockPanacheEntity.setAvailableQuantity(stock.getAvailableQuantity());
            stockPanacheEntity.setReservedQuantity(stock.getReservedQuantity());
            stockPanacheEntity.setSoldQuantity(stock.getSoldQuantity());
            return;
        }

        // Create new stock
        ProductPanacheEntity productPanacheEntity = getEntityManager().getReference(ProductPanacheEntity.class, productId);
        StockPanacheEntity newStockPanacheEntity = toPanacheEntity(stock);
        newStockPanacheEntity.setProduct(productPanacheEntity);
        persist(newStockPanacheEntity);
    }
}
