package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;
import store.andefi.dto.StockDto;
import store.andefi.dto.StockUpdateDto;
import store.andefi.entity.Stock;
import store.andefi.repository.StockRepository;
import store.andefi.utility.mapper.StockMapper;

@ApplicationScoped
public class StockService {
  @Inject StockRepository stockRepository;

  public StockDto getProductStock(String productId) {
    return stockRepository
        .find("product.id", UUID.fromString(productId))
        .firstResultOptional()
        .map(StockMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public void updateProductStock(StockUpdateDto stockUpdateDto) {
    Stock stockEntity =
        stockRepository
            .find("product.id", UUID.fromString(stockUpdateDto.getProductId()))
            .firstResultOptional()
            .orElseThrow(NotFoundException::new);

    long stockAvailableQuantity =
        stockUpdateDto.getQuantityOnHand() - stockUpdateDto.getReservedQuantity();

    stockEntity.setQuantityOnHand(stockUpdateDto.getQuantityOnHand());
    stockEntity.setReservedQuantity(stockUpdateDto.getReservedQuantity());
    stockEntity.setAvailableQuantity(stockAvailableQuantity);
    stockEntity.setSoldQuantity(stockUpdateDto.getSoldQuantity());

    stockRepository.persist(stockEntity);
  }
}
