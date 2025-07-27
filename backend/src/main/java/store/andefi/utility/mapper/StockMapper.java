package store.andefi.utility.mapper;

import store.andefi.dto.StockDto;
import store.andefi.entity.Stock;

public final class StockMapper {
  public static StockDto toDto(Stock stockEntity) {
    StockDto stockDto = new StockDto();
    stockDto.setId(stockEntity.getId().toString());
    stockDto.setProductId(stockEntity.getProduct().getId().toString());
    stockDto.setQuantityOnHand(stockEntity.getQuantityOnHand());
    stockDto.setReservedQuantity(stockEntity.getReservedQuantity());
    stockDto.setAvailableQuantity(stockEntity.getAvailableQuantity());
    stockDto.setSoldQuantity(stockEntity.getSoldQuantity());

    return stockDto;
  }
}
