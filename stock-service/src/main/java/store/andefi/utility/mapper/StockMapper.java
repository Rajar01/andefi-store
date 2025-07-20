package store.andefi.utility.mapper;

import store.andefi.dto.StockDto;
import store.andefi.entity.Stock;

public final class StockMapper {
    public static StockDto toDto(Stock stock) {
        return new StockDto(stock.getProductId(), stock.getQuantityOnHand(), stock.getReservedQuantity(), stock.getAvailableQuantity());
    }
}
