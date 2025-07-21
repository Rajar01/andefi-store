package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import store.andefi.dto.StockDto;
import store.andefi.dto.StockUpdateDto;
import store.andefi.entity.Stock;
import store.andefi.repository.StockRepository;
import store.andefi.utility.mapper.StockMapper;

@ApplicationScoped
public class StockService {
    @Inject
    StockRepository stockRepository;

    public StockDto getProductStock(String productId) {
        return stockRepository.find("productId", productId).firstResultOptional().map(StockMapper::toDto).orElseThrow(NotFoundException::new);
    }

    public void updateProductStock(StockUpdateDto stockUpdateDto) {
        Stock stockEntity = stockRepository.find("productId", stockUpdateDto.productId()).firstResultOptional().orElseThrow(NotFoundException::new);

        stockEntity.setQuantityOnHand(stockUpdateDto.quantityOnHand());
        stockEntity.setReservedQuantity(stockUpdateDto.reservedQuantity());
        stockEntity.setAvailableQuantity(stockUpdateDto.quantityOnHand() - stockUpdateDto.reservedQuantity());

        stockRepository.persist(stockEntity);
    }
}
