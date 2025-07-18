package store.andefi.utility.mapper;

import store.andefi.dto.ProductDto;
import store.andefi.entity.Product;

public final class ProductMapper {
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.id().toString(),
                product.name(),
                product.description(),
                product.price(),
                null,
                product.categoryIds(),
                product.attributes(),
                null
        );
    }
}
